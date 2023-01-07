package com.daou.batch.job;

import java.time.LocalDate;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.SimpleJobBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.daou.batch.common.service.SlackService;
import com.daou.batch.repository.DailySummaryRepository;
import com.daou.batch.repository.HourlyDataRepository;
import com.daou.batch.repository.HourlyRowDataRepository;
import com.daou.batch.repository.HourlySummaryRepository;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RollBackConfiguration {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	private final HourlyRowDataRepository rowDataRepository;
	private final HourlyDataRepository dataRepository;
	private final HourlySummaryRepository hourlySummaryRepository;
	private final DailySummaryRepository dailySummaryRepository;
	private final SlackService slackService;

	private static final String JOB_NAME = "RollbackProcess";


	@Bean
	public Job rollbackProcess() {
		return new SimpleJobBuilder(jobBuilderFactory.get(JOB_NAME))
			.start(rollbackRowData(null))
				.on(ExitStatus.FAILED.getExitCode())
			    .to(rollbackFail(null))
			    .on("*")
			    .end()
			.next(rollbackHourlyData(null))
				.on(ExitStatus.FAILED.getExitCode())
				.to(rollbackFail(null))
				.on("*")
				.end()
			.next(rollbackHourlySummary(null))
				.on(ExitStatus.FAILED.getExitCode())
				.to(rollbackFail(null))
				.on("*")
				.end()
			.next(rollbackDailySummary(null))
				.on(ExitStatus.FAILED.getExitCode())
				.to(rollbackFail(null))
				.on("*")
				.end()
			.end()
			.build();
	}

	@JobScope
	@Bean
	public Step rollbackRowData(@Value("#{jobParameters[batchDate]}") String batchDate) {

		return stepBuilderFactory.get("rollbackRowData" + batchDate).allowStartIfComplete(true)
			.tasklet(((contribution, chunkContext) -> {
				rowDataRepository.deleteByCreatedDate(LocalDate.parse(batchDate));
				slackService.postSlackMessage( "스케줄 배치 RowData 롤백 수행 요청!");
				return RepeatStatus.FINISHED;
			})).build();
	}

	@JobScope
	@Bean
	public Step rollbackHourlyData(@Value("#{jobParameters[batchDate]}") String batchDate) {

		return stepBuilderFactory.get("rollbackHourlyData" + batchDate).allowStartIfComplete(true)
			.tasklet(((contribution, chunkContext) -> {
				dataRepository.deleteByCreatedDate(LocalDate.parse(batchDate));
				slackService.postSlackMessage( "스케줄 배치 HourlyData 롤백 수행 요청!");
				return RepeatStatus.FINISHED;
			})).build();
	}

	@JobScope
	@Bean
	public Step rollbackHourlySummary(@Value("#{jobParameters[batchDate]}") String batchDate) {

		return stepBuilderFactory.get("rollbackHourlySummary" + batchDate).allowStartIfComplete(true)
			.tasklet(((contribution, chunkContext) -> {
				hourlySummaryRepository.deleteByCreatedDate(LocalDate.parse(batchDate));
				slackService.postSlackMessage( "스케줄 배치 HourlySummary 롤백 수행 요청!");
				return RepeatStatus.FINISHED;
			})).build();
	}

	@JobScope
	@Bean
	public Step rollbackDailySummary(@Value("#{jobParameters[batchDate]}") String batchDate) {

		return stepBuilderFactory.get("rollbackDailySummaryData" + batchDate).allowStartIfComplete(true)
			.tasklet(((contribution, chunkContext) -> {
				dailySummaryRepository.deleteByCreatedDate(LocalDate.parse(batchDate));
				slackService.postSlackMessage( "스케줄 배치 DailySummary 롤백 수행 요청!");
				return RepeatStatus.FINISHED;
			})).build();
	}

	@JobScope
	@Bean
	public Step rollbackFail(@Value("#{jobParameters[batchDate]}") String batchDate) {

		return stepBuilderFactory.get("RollBackFail" + batchDate).allowStartIfComplete(true)
			.tasklet(((contribution, chunkContext) -> {
				slackService.postSlackMessage("스케줄 배치 롤백 수행 실패!");
				return RepeatStatus.FINISHED;
			})).build();
	}
}
