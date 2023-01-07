package com.daou.batch.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import com.daou.batch.job.LoadFileAndSaveConfiguration;

@Slf4j
@Component
public class FileManager {

	@Value("${batch.input.path}")
	private String readPath;

	@Value("${batch.absolute.path}")
	private String filePath;

	public void zipOriginFile() throws IOException {
		String allFilePath = readPath + "/*.*";
		File saveFile = new File(filePath+"/success/"+
		//Paths.get(filePath, "success",
			LoadFileAndSaveConfiguration.TX_DATE + ".zip");
		log.info(String.valueOf(saveFile.getAbsoluteFile()));
		log.info(saveFile.getPath());
		PathMatchingResourcePatternResolver pathResolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = pathResolver.getResources(allFilePath);
		try (final FileOutputStream fos = new FileOutputStream(saveFile, true);
			 ZipOutputStream zos = new ZipOutputStream(fos)) {
			for (Resource resource : resources) {
				try (FileInputStream fis = new FileInputStream(resource.getFile().toString())) {
					ZipEntry zipEntry = new ZipEntry(Objects.requireNonNull(resource.getFilename()));
					zos.putNextEntry(zipEntry);

					byte[] bytes = new byte[1024];
					int length;
					while ((length = fis.read(bytes)) >= 0) {
						zos.write(bytes, 0, length);
					}
				} catch (IOException e) {
					log.error("압축중 에러 발생", e);
				}
			}
		} catch (FileNotFoundException e) {
			log.error("압축중 에러 발생", e);
			throw new RuntimeException(e);
		}
	}

	public void deleteOriginFile() {
		File[] fileList = new File(filePath).listFiles();
		assert fileList != null;
		for (File file : fileList) {
			if (file.isFile() && file.exists()) {
				file.delete();
			}
		}
	}
}
