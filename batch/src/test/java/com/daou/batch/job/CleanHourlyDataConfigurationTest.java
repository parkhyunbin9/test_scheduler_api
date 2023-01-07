package com.daou.batch.job;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.daou.batch.TestBatchConfig;
import com.daou.batch.dto.HourlyDataDto;
import com.daou.batch.model.HourlyData;
import com.daou.batch.model.HourlyRowData;
import com.daou.batch.repository.HourlyDataRepository;
import com.daou.batch.repository.HourlyRowDataRepository;

@SpringBatchTest
@SpringBootTest(classes = {CleanHourlyDataConfiguration.class, TestBatchConfig.class},
properties = "spring.profiles.active=test")
class CleanHourlyDataConfigurationTest {

	static LocalDate TEST_DATE = LocalDate.now().minusDays(-1);

	@Autowired
	JpaPagingItemReader<HourlyDataDto> reader;
	@Autowired
	ItemProcessor<HourlyDataDto, HourlyData> processor;
	@Autowired
	HourlyRowDataRepository rowRepository;
	@Autowired
	HourlyDataRepository repository;
	@Autowired
	JobLauncherTestUtils jobLauncherTestUtils;

	@AfterEach
	public void tearDown() throws Exception {
		reader.close();
		rowRepository.deleteAll();
		repository.deleteAll();
	}

	@Test
	@DisplayName("RowData를 읽어서 HourlyDataDto로 변환")
	void rowDataToHourlyDataDtoTest() throws Exception {

		HourlyRowData rowData1 = HourlyRowData.builder().date(TEST_DATE).hour(0)
			.newUser(10L)
			.churnUser(10L)
			.payAmount(BigDecimal.valueOf(100L))
			.cost(BigDecimal.valueOf(100L))
			.salesAmount(BigDecimal.valueOf(100L))
			.build();
		HourlyRowData rowData2 = HourlyRowData.builder().date(TEST_DATE).hour(0)
			.newUser(20L)
			.churnUser(20L)
			.payAmount(BigDecimal.valueOf(200L))
			.cost(BigDecimal.valueOf(200L))
			.salesAmount(BigDecimal.valueOf(200L))
			.build();
		HourlyRowData rowData3 = HourlyRowData.builder().date(TEST_DATE).hour(0)
			.newUser(30L)
			.churnUser(30L)
			.payAmount(BigDecimal.valueOf(300L))
			.cost(BigDecimal.valueOf(300L))
			.salesAmount(BigDecimal.valueOf(300L))
			.build();

		rowRepository.save(rowData1);
		rowRepository.save(rowData2);
		rowRepository.save(rowData3);

		reader.open(new ExecutionContext());

		HourlyDataDto result = reader.read();
		assertThat(result.getNewUser()).isEqualTo(
			rowData1.getNewUser() + rowData2.getNewUser() + rowData3.getNewUser());
		assertThat(result.getChurnUser()).isEqualTo(
			rowData1.getChurnUser() + rowData2.getChurnUser() + rowData3.getChurnUser());

		assertThat(result.getPayAmount()).isEqualByComparingTo(
			rowData1.getPayAmount().add(rowData2.getPayAmount()).add(rowData3.getPayAmount()));
		assertThat(result.getPayAmount()).isEqualByComparingTo(
			rowData1.getPayAmount().add(rowData2.getPayAmount()).add(rowData3.getPayAmount()));
		assertThat(result.getPayAmount()).isEqualByComparingTo(
			rowData1.getPayAmount().add(rowData2.getPayAmount()).add(rowData3.getPayAmount()));
	}

	@DisplayName("HourlyDataDto를 HourlyData로 변환")
	@Test
	void hourlyDataDtoToHourlyData() throws Exception {
		HourlyRowData rowData1 = HourlyRowData.builder().date(TEST_DATE).hour(0)
			.newUser(10L)
			.churnUser(10L)
			.payAmount(BigDecimal.valueOf(100L))
			.cost(BigDecimal.valueOf(100L))
			.salesAmount(BigDecimal.valueOf(100L))
			.build();
		HourlyRowData rowData2 = HourlyRowData.builder().date(TEST_DATE).hour(0)
			.newUser(20L)
			.churnUser(20L)
			.payAmount(BigDecimal.valueOf(200L))
			.cost(BigDecimal.valueOf(200L))
			.salesAmount(BigDecimal.valueOf(200L))
			.build();
		HourlyRowData rowData3 = HourlyRowData.builder().date(TEST_DATE).hour(0)
			.newUser(30L)
			.churnUser(30L)
			.payAmount(BigDecimal.valueOf(300L))
			.cost(BigDecimal.valueOf(300L))
			.salesAmount(BigDecimal.valueOf(300L))
			.build();

		rowRepository.save(rowData1);
		rowRepository.save(rowData2);
		rowRepository.save(rowData3);

		reader.open(new ExecutionContext());
		HourlyDataDto result = reader.read();
		HourlyData convertedResult = processor.process(result);

		assertThat(convertedResult.getNewUser()).isEqualTo(result.getNewUser());
		assertThat(convertedResult.getChurnUser()).isEqualTo(result.getNewUser());
		assertThat(convertedResult.getPayAmount()).isEqualTo(result.getPayAmount());
		assertThat(convertedResult.getCost()).isEqualTo(result.getCost());
		assertThat(convertedResult.getSalesAmount()).isEqualTo(result.getSalesAmount());
		assertThat(convertedResult).isInstanceOf(HourlyData.class);

	}

	@DisplayName("CleanHourlyData 통합테스트")
	@Test
	void cleanHourlyDataJobTest() throws Exception {
		JobExecution jobExecution = jobLauncherTestUtils.launchJob();
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
	}
}