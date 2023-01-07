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
import com.daou.batch.dto.HourlySummaryDto;
import com.daou.batch.model.HourlyData;
import com.daou.batch.model.HourlySummary;
import com.daou.batch.repository.HourlyDataRepository;
import com.daou.batch.repository.HourlySummaryRepository;

@SpringBatchTest
@SpringBootTest(classes = {HourlySummaryConfiguration.class, TestBatchConfig.class},
	properties = "spring.profiles.active=test")
class HourlySummaryConfigurationTest {

	static LocalDate TEST_DATE = LocalDate.now().minusDays(-1);
	static LocalDate TEST_DATE_MINUS_ONE_DAY = LocalDate.now().minusDays(-2);
	static LocalDate TEST_DATE_MINUS_TWO_DAY = LocalDate.now().minusDays(-3);

	@Autowired
	JpaPagingItemReader<HourlySummaryDto> reader;
	@Autowired
	ItemProcessor<HourlySummaryDto, HourlySummary> processor;
	@Autowired
	HourlyDataRepository hourRepository;
	@Autowired
	HourlySummaryRepository repository;
	@Autowired
	JobLauncherTestUtils jobLauncherTestUtils;

	@AfterEach
	void tearDown() throws Exception {
		reader.close();
		hourRepository.deleteAll();
		repository.deleteAll();
	}

	@DisplayName("1차 집계된 데이터를 시간별 누적 데이터로 변환")
	@Test
	void hourlyDataToHourlySummaryDto() throws Exception {

		HourlyData rowData1 = HourlyData.builder().date(TEST_DATE)
			.hour(1)
			.newUser(10L)
			.churnUser(10L)
			.payAmount(BigDecimal.valueOf(100L))
			.cost(BigDecimal.valueOf(100L))
			.salesAmount(BigDecimal.valueOf(100L))
			.build();
		HourlyData rowData2 = HourlyData.builder().date(TEST_DATE_MINUS_ONE_DAY)
			.hour(1)
			.newUser(20L)
			.churnUser(20L)
			.payAmount(BigDecimal.valueOf(200L))
			.cost(BigDecimal.valueOf(200L))
			.salesAmount(BigDecimal.valueOf(200L))
			.build();
		HourlyData rowData3 = HourlyData.builder().date(TEST_DATE_MINUS_TWO_DAY)
			.hour(1)
			.newUser(30L)
			.churnUser(30L)
			.payAmount(BigDecimal.valueOf(300L))
			.cost(BigDecimal.valueOf(300L))
			.salesAmount(BigDecimal.valueOf(300L))
			.build();

		hourRepository.save(rowData1);
		hourRepository.save(rowData2);
		hourRepository.save(rowData3);

		reader.open(new ExecutionContext());

		HourlySummaryDto result = reader.read();
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

	@DisplayName("HourlySummaryDto를 HourlySummary로 변환")
	@Test
	void hourlySummaryDtoToHourlySummary() throws Exception {
		HourlyData rowData1 = HourlyData.builder().date(TEST_DATE)
			.hour(1)
			.newUser(10L)
			.churnUser(10L)
			.payAmount(BigDecimal.valueOf(100L))
			.cost(BigDecimal.valueOf(100L))
			.salesAmount(BigDecimal.valueOf(100L))
			.build();
		HourlyData rowData2 = HourlyData.builder().date(TEST_DATE_MINUS_ONE_DAY)
			.hour(1)
			.newUser(20L)
			.churnUser(20L)
			.payAmount(BigDecimal.valueOf(200L))
			.cost(BigDecimal.valueOf(200L))
			.salesAmount(BigDecimal.valueOf(200L))
			.build();
		HourlyData rowData3 = HourlyData.builder().date(TEST_DATE_MINUS_TWO_DAY)
			.hour(1)
			.newUser(30L)
			.churnUser(30L)
			.payAmount(BigDecimal.valueOf(300L))
			.cost(BigDecimal.valueOf(300L))
			.salesAmount(BigDecimal.valueOf(300L))
			.build();

		hourRepository.save(rowData1);
		hourRepository.save(rowData2);
		hourRepository.save(rowData3);

		reader.open(new ExecutionContext());
		HourlySummaryDto result = reader.read();
		HourlySummary convertedResult = processor.process(result);
		assertThat(convertedResult).isInstanceOf(HourlySummary.class);

		assertThat(convertedResult.getSumNewUser()).isEqualTo(result.getSumNewUser());
		assertThat(convertedResult.getSumChurnUser()).isEqualTo(result.getSumChurnUser());
		assertThat(convertedResult.getSumPayAmount()).isEqualTo(result.getSumPayAmount());
		assertThat(convertedResult.getSumCost()).isEqualTo(result.getSumCost());
		assertThat(convertedResult.getSumSalesAmount()).isEqualTo(result.getSumSalesAmount());
	}

	@DisplayName("HourlySummaryJob 통합 테스트")
	@Test
	void hourlySummaryJobTest() throws Exception {
		JobExecution jobExecution = jobLauncherTestUtils.launchJob();
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
	}
}