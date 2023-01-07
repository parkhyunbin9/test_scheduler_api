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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.daou.batch.common.DayUnit;
import com.daou.batch.common.service.SlackService;
import com.daou.batch.dto.DailySummaryDto;
import com.daou.batch.model.DailySummary;
import com.daou.batch.repository.DailySummaryRepository;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class DailySummaryConfiguration {

	public static final String JOB_NAME = "DailySummaryJob";
	public static final int chunkSize = DayUnit.ONE_YEAR.getDays();
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final EntityManagerFactory entityManagerFactory;
	private final DailySummaryRepository repository;
	private final SlackService slackService;

	@Bean(name = JOB_NAME)
	public Job dailySummaryProcess() throws Exception {
		return new SimpleJobBuilder(jobBuilderFactory.get(JOB_NAME)
			.incrementer(new RunIdIncrementer()))
			.start(dailySummary())
				.on(ExitStatus.FAILED.getExitCode())
				.to(processError(null))
				.on("*")
				.end()
			.from(dailySummary())
				.on("*")
				.end()
			.end()
			.build();
	}


	@Bean(name = JOB_NAME + "Step")
	public Step dailySummary() throws Exception {
		return stepBuilderFactory.get(JOB_NAME)
			.allowStartIfComplete(true)
			.<DailySummaryDto, DailySummary>chunk(chunkSize)
			.reader(reader())
			.processor(processor())
			.writer(writer())
			.build();
	}

	@Bean(JOB_NAME + READER)
	public JpaPagingItemReader<DailySummaryDto> reader() {

		String sql = "select new com.daou.batch.dto.DailySummaryDto(" +
			"h.date" +
			", sum(h.newUser), sum(h.churnUser), sum(h.payAmount), sum(h.cost), sum(h.salesAmount) " +
			", avg(h.newUser), avg(h.churnUser), cast(avg(h.payAmount) as double), cast(avg(h.cost) as double), cast(avg(h.salesAmount) as double) "
			+
			", max(h.newUser), max(h.churnUser), max(h.payAmount), max(h.cost), max(h.salesAmount)" +
			", min(h.newUser), min(h.churnUser), min(h.payAmount), min(h.cost), min(h.salesAmount) " +
			") " +
			"from hourly_data h " +
			"group by h.date";

		return new JpaPagingItemReaderBuilder<DailySummaryDto>()
			.name("dailyDataReader")
			.entityManagerFactory(entityManagerFactory)
			.pageSize(chunkSize)
			.queryString(sql)
			.build();
	}

	@Bean(JOB_NAME + PROCESSOR)
	public ItemProcessor<DailySummaryDto, DailySummary> processor() {
		return DailySummaryDto::toEntity;
	}

	@Bean(JOB_NAME + WRITER)
	public JpaItemWriter<DailySummary> writer() {
		JpaItemWriter<DailySummary> jpaItemWriter = new JpaItemWriter<>();
		jpaItemWriter.setUsePersist(true);
		jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
		return jpaItemWriter;
	}

	@JobScope
	@Bean(JOB_NAME + ERROR_PROCESSOR)
	public Step processError( @Value("#{jobParameters[batchDate]}") String batchDate) {
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