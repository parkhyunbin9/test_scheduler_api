package com.daou.batch.job;

import static com.daou.batch.common.StepConst.*;

import java.time.LocalDate;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.SimpleJobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.daou.batch.common.service.SlackService;
import com.daou.batch.dto.HourlyDataDto;
import com.daou.batch.model.HourlyData;
import com.daou.batch.repository.HourlyDataRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class CleanHourlyDataConfiguration {

	public static final String JOB_NAME = "CleanHourlyData";
	public static final int chunkSize = 1000;
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final EntityManagerFactory entityManagerFactory;
	private final HourlyDataRepository repository;
	private final SlackService slackService;

	@Bean(name = JOB_NAME)
	public Job cleanHourlyDataProcess() throws Exception {
		return new SimpleJobBuilder(jobBuilderFactory.get(JOB_NAME)
			.incrementer(new RunIdIncrementer()))
			.start(cleanHourlyData())
			.on(ExitStatus.FAILED.getExitCode())
			.to(processError(null))
			.on("*")
			.end()
			.from(cleanHourlyData())
			.on("*")
			.end()
			.end()
			.build();
	}

	@Bean(name = JOB_NAME + "Step")
	public Step cleanHourlyData() throws Exception {
		return stepBuilderFactory.get(JOB_NAME)
			.allowStartIfComplete(true)
			.<HourlyDataDto, HourlyData>chunk(chunkSize)
			.reader(reader())
			.processor(processor())
			.writer(writer())
			.build();
	}

	@Bean(JOB_NAME + READER)
	public JpaPagingItemReader<HourlyDataDto> reader() {

		String sql = "select new com.daou.batch.dto.HourlyDataDto(" +
			"r.date, r.hour, sum(r.newUser), sum(r.churnUser), sum(r.payAmount), sum(r.cost), sum(r.salesAmount) " +
			") " +
			"from hourly_row_data r " +
			"group by r.date, r.hour";

		return new JpaPagingItemReaderBuilder<HourlyDataDto>()
			.name("hourlyRowDataReader")
			.entityManagerFactory(entityManagerFactory)
			.pageSize(chunkSize)
			.queryString(sql)
			.build();
	}

	@Bean(JOB_NAME + PROCESSOR)
	public ItemProcessor<HourlyDataDto, HourlyData> processor() {
		return HourlyDataDto::toEntity;
	}

	@Bean(JOB_NAME + WRITER)
	public JpaItemWriter<HourlyData> writer() {
		JpaItemWriter<HourlyData> jpaItemWriter = new JpaItemWriter<>();
		jpaItemWriter.setUsePersist(true);
		jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
		return jpaItemWriter;
	}

	@JobScope
	@Bean(JOB_NAME + ERROR_PROCESSOR)
	public Step processError(@Value("#{jobParameters[batchDate]}") String batchDate) {
		return stepBuilderFactory.get("errorProcessor" + batchDate).allowStartIfComplete(true)
			.tasklet(((contribution, chunkContext) -> {
				String errorMessage = JOB_NAME + " 배치 요청일 : " + batchDate + " 에러 발생";
				log.error(errorMessage);
				slackService.postSlackMessage(errorMessage + " 롤백 수행 요청!");
				errorRollBackByDate();
				return RepeatStatus.FINISHED;
			})).build();
	}

	public void errorRollBackByDate() {
		repository.deleteByCreatedDate(LocalDate.now());
		slackService.postSlackMessage(JOB_NAME + " 롤백 수행 완료!");
	}
}