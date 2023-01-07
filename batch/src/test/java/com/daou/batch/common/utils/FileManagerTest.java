package com.daou.batch.common.utils;

import static org.assertj.core.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.daou.batch.TestBatchConfig;

@SpringBootTest(classes = {FileManager.class, TestBatchConfig.class}, properties = "spring.profiles.active=test")
class FileManagerTest {

	String testFile = "testData.text";
	@Autowired
	FileManager fileManager;

	@AfterAll
	public static void tearDown() {
		File zipedPath = Paths.get("src", "test", "data", "success").toFile();
		File savedFile = Paths.get("src", "test", "data").toFile();

		if (Objects.nonNull(savedFile)) {
			savedFile.listFiles()[0].delete();
		}

		if (Objects.nonNull(zipedPath.listFiles()[0])) {
			zipedPath.listFiles()[0].delete();
		}
	}

	@BeforeEach
	public void init() throws IOException {
		Path path = Paths.get("src", "test", "data", testFile);
		path.getParent().toFile().mkdir();
		path.toFile().createNewFile();
	}

	@DisplayName("특정 경로의 파일을 압축")
	@Test
	void zipFileFromPath() throws IOException {
		String zipFileName = LocalDate.now().toString() + ".zip";
		Path savedPath = Paths.get("src", "test", "data");
		assertThat(Arrays.toString(savedPath.toFile().list())).contains(testFile);

		fileManager.zipOriginFile();
		Path zipFilePath = Paths.get("src", "test", "data","success");
		assertThat(Arrays.toString(zipFilePath.toFile().list())).contains(zipFileName);
	}

	@DisplayName("특정 경로의 파일을 삭제")
	@Test
	void removeFromPath() {
		Path savedPath = Paths.get("src", "test", "data");

		assertThat(Arrays.toString(savedPath.toFile().list())).contains(testFile);
		fileManager.deleteOriginFile();
		assertThat(Arrays.toString(savedPath.toFile().list())).doesNotContain(testFile);

	}

}