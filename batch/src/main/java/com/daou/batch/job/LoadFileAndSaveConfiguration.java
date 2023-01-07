package com.daou.batch.job;

import static com.daou.batch.common.StepConst.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.SimpleJobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.validator.ValidatingItemProcessor;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.daou.batch.common.FileType;
import com.daou.batch.common.service.SlackService;
import com.daou.batch.common.utils.FileManager;
import com.daou.batch.common.validation.RowDataDtoValidator;
import com.daou.batch.dto.HourlyRowDataDto;
import com.daou.batch.reader.FileFromPathReader;
import com.daou.batch.repository.HourlyRowDataRepository;
import com.daou.batch.writer.RowDataWriter;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class LoadFileAndSaveConfiguration {

	public static final String JOB_NAME = "loadFileAndSave";
	public static final LocalDate TX_DATE = LocalDate.now();
	public static final int chunkSize = 1000;
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final RowDataWriter writer;
	private final HourlyRowDataRepository repository;
	private final FileFromPathReader reader;
	private final SlackService slackService;
	private final FileManager fileManager;
	/**
	 * TODO: 2. 실패시 처리 하지 않음 (transaction) 0
	 * TODO: 3. 로깅
	 * TODO: 4. 실패시 slack api 호출로 노티 0
	 * TODO: 5. DTO 분류 0
	 * TODO: 5. 기능별 테스트 케이스 작성
	 *
	 * 테스트 케이스 추가 +  1차 집계가 끝나고 2차 날짜별 시간대별 중복값 sum해서 집계
	 * 날짜별 시간대별 독립적인 값을 가지도록
	 * 1. row data 생성 기본 집계 > 종료시 압축하여 /success / date.zip파일로 저장
	 * 1. 날짜별 시간대별 데이터 1차 집계
	 * 2. 시간대별 데이터 (24row) 누적 2차 집계
	 * 3. 날짜별 데이터  2차 집계
	 *
	 * 파이프 라인 기본 집계 -> 1차 집계 -> 2차 집계 순으로 동작
	 *
	 * 1. 테스트할 항목 제대로 validation 동작하는지
	 * 2. validation시 롤백 되는지
	 * 3. 에러시 롤백 처리 잘 되는지
	 * 4. 압축 파일이 잘 생성되는지
	 * 5. 압축 파일내 원래 있던 파일 명들이 다 있는지
	 * 6. 원래있던 파일들이 잘 제거 되는지
	 * 7. 로드된 파일 데이터들이 잘 적재되는 지
	 * 8. 이력들이 제대로 저장되는지
	 */

	@Bean(name = JOB_NAME)
	public Job loadAndSaveProcess() throws Exception {

		SimpleJobBuilder job =
			new SimpleJobBuilder(jobBuilderFactory.get(JOB_NAME)
				.incrementer(new RunIdIncrementer()));

		List<Step> steps = getFileStepByPath();
		Step lastStep = null;
		for (Step step : steps) {
			job = (steps.indexOf(step) == 0) ? job.start(step) : job.next(step);
			if (steps.indexOf(step) == steps.size() - 1) {
				lastStep = step;
			}
			job.on(ExitStatus.FAILED.getExitCode())
				.to(processError(null))
				.on("*")
				.end();



			// 스텝을 순회하며 수행하는데 실패시 에러 처리 후 실패 /그외는 성공
		}

		return job
				.on(ExitStatus.COMPLETED.getExitCode())
				.to(zipOriginFile())
				.next(deleteOriginFile())
				.on("*")
				.end()
			.end()
			.build();
	}

	@Bean(name = JOB_NAME + "Step")
	public List<Step> getFileStepByPath() throws Exception {

		List<Step> steps = new ArrayList<>();

		for (FileType type : FileType.values()) {

			steps.add(stepBuilderFactory.get("getFileStepByPath" + type + TX_DATE)
				.allowStartIfComplete(true)
				.<HourlyRowDataDto, HourlyRowDataDto>chunk(chunkSize)
				.reader(reader.readFiles(type))
				.processor(validatorProcessor())
				.writer(writer)
				.build());
		}
		return steps;
	}

	@Bean
	public ValidatingItemProcessor<HourlyRowDataDto> validatorProcessor() throws ValidationException {
		ValidatingItemProcessor<HourlyRowDataDto> validatorProcessor = new ValidatingItemProcessor<>();
		validatorProcessor.setValidator(new RowDataDtoValidator());
		return validatorProcessor;
	}

	@Bean
	public Step zipOriginFile() {
		return stepBuilderFactory.get("zipOriginFile" + TX_DATE)
			.allowStartIfComplete(true)
			.tasklet(((contribution, chunkContext) -> {
				fileManager.zipOriginFile();
				return RepeatStatus.FINISHED;
			})).build();
	}

	@Bean
	public Step deleteOriginFile() {
		return stepBuilderFactory.get("deleteOriginFile" + TX_DATE)
			.allowStartIfComplete(true)
			.tasklet(((contribution, chunkContext) -> {
				fileManager.deleteOriginFile();
				return RepeatStatus.FINISHED;
			})).build();
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