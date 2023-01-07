package com.daou.batch.job;

import static org.assertj.core.api.Assertions.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.daou.batch.TestBatchConfig;
import com.daou.batch.common.FileType;
import com.daou.batch.common.utils.FileManager;
import com.daou.batch.dto.HourlyRowDataDto;
import com.daou.batch.reader.FileFromPathReader;
import com.daou.batch.repository.HourlyRowDataRepository;
import com.daou.batch.writer.RowDataWriter;

@SpringBatchTest
@SpringBootTest(classes = {LoadFileAndSaveConfiguration.class, TestBatchConfig.class, FileManager.class, FileFromPathReader.class, RowDataWriter.class},
	properties = "spring.profiles.active=test")
class LoadFileAndSaveConfigurationTest {

	@Autowired
	FileFromPathReader reader;
	@Autowired
	HourlyRowDataRepository repository;
	@Autowired
	JobLauncherTestUtils jobLauncherTestUtils;

	String textFile = "testData.text";
	String csvFile = "testData.csv";
	String txtFile = "testData.txt";
	String textDate = "2022-01-01";
	String txtDate = "2022-02-01";
	String csvDate = "2022-03-01";

	String textHour = "0";
	String txtHour = "1";
	String csvHour = "2";

	String newUser = "10";
	String churnUser = "20";
	String payAmount = "30000";
	String cost = "40000";
	String salesAmount = "50000";

	@BeforeEach
	public void init() throws IOException {
		Path textPath = Paths.get("src", "main", "resources", "data", "testData", textFile);
		textPath.toFile().createNewFile();
		String textData = "시간|가입자수|탈퇴자수|결제 금액|사용금액|매출 금액\n" +
			textDate + " " + textHour + "|" + newUser + "|" + churnUser + "|" + payAmount + "|" + cost + "|"
			+ salesAmount + "\n" +
			textDate + " " + "2" + textHour + "|" + newUser + "|" + churnUser + "|" + payAmount + "|" + cost + "|"
			+ salesAmount;
		Files.write(textPath, textData.getBytes());

		Path txtPath = Paths.get("src", "main", "resources", "data", "testData", txtFile);
		txtPath.toFile().createNewFile();
		String txtData = "시간/가입자수/탈퇴자수/결제 금액/사용금액/매출 금액\n" +
			txtDate + " " + txtHour + "/" + newUser + "/" + churnUser + "/" + payAmount + "/" + cost + "/" + salesAmount
			+ "\n" +
			txtDate + " " + "2" + txtHour + "/" + newUser + "/" + churnUser + "/" + payAmount + "/" + cost + "/"
			+ salesAmount;
		Files.write(txtPath, txtData.getBytes());

		Path csvPath = Paths.get("src", "main", "resources", "data", "testData", csvFile);
		csvPath.toFile().createNewFile();
		String csvData = "시간|가입자수|탈퇴자수|결제 금액|사용금액|매출 금액\n" +
			csvDate + " " + csvHour + "," + newUser + "," + churnUser + "," + payAmount + "," + cost + "," + salesAmount
			+ "\n" +
			csvDate + " " + "2" + csvHour + "," + newUser + "," + churnUser + "," + payAmount + "," + cost + ","
			+ salesAmount;
		Files.write(csvPath, csvData.getBytes());
	}

	@AfterEach
	public void clean() {
		repository.deleteAll();
	}

	@DisplayName("경로의 다양한 형식의 파일을 HourlyRowDataDto로 변환")
	@Test
	void loadFileFromPathToHourlyRowDataDto() throws Exception {

		List<HourlyRowDataDto> result = new ArrayList<>();

		for (FileType type : FileType.values()) {
			ItemReader<HourlyRowDataDto> itemReader = reader.readFiles(type);
			result.add(itemReader.read());
		}

		HourlyRowDataDto textDataObj = new HourlyRowDataDto(
			LocalDate.parse(textDate),
			Integer.parseInt(textHour),
			Long.parseLong(newUser),
			Long.parseLong(churnUser),
			BigDecimal.valueOf(Long.parseLong(payAmount)),
			BigDecimal.valueOf(Long.parseLong(cost)),
			BigDecimal.valueOf(Long.parseLong(salesAmount)));

		HourlyRowDataDto txtDataObj = new HourlyRowDataDto(
			LocalDate.parse(txtDate),
			Integer.parseInt(txtHour),
			Long.parseLong(newUser),
			Long.parseLong(churnUser),
			BigDecimal.valueOf(Long.parseLong(payAmount)),
			BigDecimal.valueOf(Long.parseLong(cost)),
			BigDecimal.valueOf(Long.parseLong(salesAmount)));

		HourlyRowDataDto csvDataObj = new HourlyRowDataDto(
			LocalDate.parse(csvDate),
			Integer.parseInt(csvHour),
			Long.parseLong(newUser),
			Long.parseLong(churnUser),
			BigDecimal.valueOf(Long.parseLong(payAmount)),
			BigDecimal.valueOf(Long.parseLong(cost)),
			BigDecimal.valueOf(Long.parseLong(salesAmount)));

		assertThat(result).usingRecursiveFieldByFieldElementComparator().contains(textDataObj);
		assertThat(result).usingRecursiveFieldByFieldElementComparator().containsAnyOf(txtDataObj);
		assertThat(result).usingRecursiveFieldByFieldElementComparator().containsAnyOf(csvDataObj);
	}

	@DisplayName("LoadAndSaveJob 통합 테스트")
	@Test
	void loadAndSaveJobTest() throws Exception {
		JobExecution jobExecution = jobLauncherTestUtils.launchJob();
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
	}
}