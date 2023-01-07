package com.daou.batch.job;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.daou.batch.TestBatchConfig;
import com.daou.batch.dto.DailySummaryDto;
import com.daou.batch.model.DailySummary;
import com.daou.batch.model.HourlyData;
import com.daou.batch.repository.DailySummaryRepository;
import com.daou.batch.repository.HourlyDataRepository;

@SpringBatchTest
@SpringBootTest(classes = {DailySummaryConfiguration.class, TestBatchConfig.class},
	properties = "spring.profiles.active=test")
class DailySummaryConfigurationTest {

	static LocalDate TEST_DATE = LocalDate.now().minusDays(-1);

	@Autowired
	JpaPagingItemReader<DailySummaryDto> reader;
	@Autowired
	ItemProcessor<DailySummaryDto, DailySummary> processor;
	@Autowired
	HourlyDataRepository dataRepository;
	@Autowired
	DailySummaryRepository repository;
	@Autowired
	JobLauncherTestUtils jobLauncherTestUtils;

	@AfterEach
	public void tearDown() throws Exception {
		reader.close();
		dataRepository.deleteAll();
		repository.deleteAll();
	}

	@DisplayName("1차 집계 HourlyData를 일별 누적 데이터로 변환")
	@Test
	public void hourlyDataToDailySummaryDto() throws Exception {

		HourlyData rowData1 = HourlyData.builder().date(TEST_DATE)
			.hour(1)
			.newUser(10L)
			.churnUser(10L)
			.payAmount(BigDecimal.valueOf(100L))
			.cost(BigDecimal.valueOf(100L))
			.salesAmount(BigDecimal.valueOf(100L))
			.build();
		HourlyData rowData2 = HourlyData.builder().date(TEST_DATE)
			.hour(2)
			.newUser(20L)
			.churnUser(20L)
			.payAmount(BigDecimal.valueOf(200L))
			.cost(BigDecimal.valueOf(200L))
			.salesAmount(BigDecimal.valueOf(200L))
			.build();
		HourlyData rowData3 = HourlyData.builder().date(TEST_DATE)
			.hour(2)
			.newUser(30L)
			.churnUser(30L)
			.payAmount(BigDecimal.valueOf(300L))
			.cost(BigDecimal.valueOf(300L))
			.salesAmount(BigDecimal.valueOf(300L))
			.build();

		dataRepository.save(rowData1);
		dataRepository.save(rowData2);
		dataRepository.save(rowData3);

		reader.open(new ExecutionContext());

		DailySummaryDto result = reader.read();
		assert result != null;

		assertThat(result.getSumNewUser()).isEqualTo(
			rowData1.getNewUser() + rowData2.getNewUser() + rowData3.getNewUser());
		assertThat(result.getSumChurnUser()).isEqualTo(
			rowData1.getChurnUser() + rowData2.getChurnUser() + rowData3.getChurnUser());

		assertThat(result.getSumPayAmount()).isEqualByComparingTo(
			rowData1.getPayAmount().add(rowData2.getPayAmount()).add(rowData3.getPayAmount()));
		assertThat(result.getSumCost()).isEqualByComparingTo(
			rowData1.getCost().add(rowData2.getCost()).add(rowData3.getCost()));
		assertThat(result.getSumSalesAmount()).isEqualByComparingTo(
			rowData1.getSalesAmount().add(rowData2.getSalesAmount()).add(rowData3.getSalesAmount()));

	}

	@DisplayName("DailySummaryDto를 DailySummary로 변환")
	@Test
	void dailySummaryDtoToDailySummary() throws Exception {
		HourlyData rowData1 = HourlyData.builder().date(TEST_DATE)
			.hour(1)
			.newUser(10L)
			.churnUser(10L)
			.payAmount(BigDecimal.valueOf(100L))
			.cost(BigDecimal.valueOf(100L))
			.salesAmount(BigDecimal.valueOf(100L))
			.build();
		HourlyData rowData2 = HourlyData.builder().date(TEST_DATE)
			.hour(2)
			.newUser(20L)
			.churnUser(20L)
			.payAmount(BigDecimal.valueOf(200L))
			.cost(BigDecimal.valueOf(200L))
			.salesAmount(BigDecimal.valueOf(200L))
			.build();
		HourlyData rowData3 = HourlyData.builder().date(TEST_DATE)
			.hour(2)
			.newUser(30L)
			.churnUser(30L)
			.payAmount(BigDecimal.valueOf(300L))
			.cost(BigDecimal.valueOf(300L))
			.salesAmount(BigDecimal.valueOf(300L))
			.build();

		dataRepository.save(rowData1);
		dataRepository.save(rowData2);
		dataRepository.save(rowData3);

		reader.open(new ExecutionContext());
		DailySummaryDto result = reader.read();
		DailySummary convertedResult = processor.process(result);
		assertThat(convertedResult).isInstanceOf(DailySummary.class);

		assertThat(convertedResult.getSumNewUser()).isEqualTo(result.getSumNewUser());
		assertThat(convertedResult.getSumChurnUser()).isEqualTo(result.getSumChurnUser());
		assertThat(convertedResult.getSumPayAmount()).isEqualTo(result.getSumPayAmount());
		assertThat(convertedResult.getSumCost()).isEqualTo(result.getSumCost());
		assertThat(convertedResult.getSumSalesAmount()).isEqualTo(result.getSumSalesAmount());
	}

	@DisplayName("DailySummaryJob 통합테스트")
	@Test
	void dailySummaryJobTest() throws Exception {
		JobExecution jobExecution = jobLauncherTestUtils.launchJob();
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
	}
}