package com.daou.batch;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.batch.operations.JobRestartException;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.daou.batch.common.service.SlackService;
import com.daou.batch.job.CleanHourlyDataConfiguration;
import com.daou.batch.job.DailySummaryConfiguration;
import com.daou.batch.job.HourlySummaryConfiguration;
import com.daou.batch.job.LoadFileAndSaveConfiguration;
import com.daou.batch.job.RollBackConfiguration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class BatchScheduler {

	final LocalDate batchDate = LocalDate.now();
	private final JobLauncher launcher;
	private final LoadFileAndSaveConfiguration loadFileAndSave;
	private final CleanHourlyDataConfiguration cleanHourlyData;
	private final HourlySummaryConfiguration hourlySummary;
	private final DailySummaryConfiguration dailySummary;
	private final RollBackConfiguration rollback;
	private final SlackService slackService;

	@Scheduled(cron = "0 0 0 * * *")
	public void runJob() {
		JobParameters jobParam = new JobParametersBuilder().addString("batchDate", batchDate.toString())
			.toJobParameters();
		try {

			List<Job> jobList = new LinkedList<>();
			jobList.add(loadFileAndSave.loadAndSaveProcess());
			jobList.add(cleanHourlyData.cleanHourlyDataProcess());
			jobList.add(hourlySummary.hourlySummaryProcess());
			jobList.add(dailySummary.dailySummaryProcess());

			ExitStatus status = ExitStatus.COMPLETED;

			for (Job job : jobList) {

				if (status.equals(ExitStatus.COMPLETED)) {
					JobExecution run = launcher.run(job, jobParam);
					status = run.getExitStatus();

				} else if (status.equals(ExitStatus.FAILED)) {
					log.error(">>>>>> ERROR!!!!");
					launcher.run(rollback.rollbackProcess(), jobParam);
					throw new RuntimeException("????????? ?????? ????????? ?????? ??????");
				}
				log.warn(">>>>>> STATUS = {} {}", job.getName(), status);
			}

		} catch (JobExecutionAlreadyRunningException | JobInstanceAlreadyCompleteException |
				 JobParametersInvalidException |
				 JobRestartException e) {

			log.error("Job ????????? ?????? ?????? ", e);
			slackService.postError(e.getMessage(), Arrays.toString(e.getStackTrace()));
		} catch (Exception e) {
			log.error("?????? ?????? ?????? ", e);
			slackService.postError(e.getMessage(), Arrays.toString(e.getStackTrace()));
			throw new RuntimeException(e);
		}
		slackService.postSlackMessage(LocalDate.now() + " ????????? ?????????????????????.");
	}

}
