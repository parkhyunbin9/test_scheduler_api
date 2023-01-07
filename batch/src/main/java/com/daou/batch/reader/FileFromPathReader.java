package com.daou.batch.reader;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import com.daou.batch.common.FileType;
import com.daou.batch.dto.HourlyRowDataDto;
import com.daou.batch.reader.mapper.RowDataMapper;

@Slf4j
@Component
public class FileFromPathReader implements FileReader<FileType> {

	@Value("${batch.input.path}")
	private String readPath;

	@Value("${batch.absolute.path}")
	private String filePath;

	@Override
	public ItemReader<HourlyRowDataDto> readFiles(FileType type) throws Exception {
		PathMatchingResourcePatternResolver pathResolver = new PathMatchingResourcePatternResolver();
		MultiResourceItemReader<HourlyRowDataDto> multiItemReader = new MultiResourceItemReader<>();

		String formattedFilePath = type.getAllFilesInPathWithFormatPattern(readPath);
		Resource[] resources = pathResolver.getResources(formattedFilePath);

		multiItemReader.setResources(resources);
		multiItemReader.setDelegate((ResourceAwareItemReaderItemStream<? extends HourlyRowDataDto>)getItemReader(type));
		multiItemReader.open(new ExecutionContext());

		return multiItemReader;
	}

	@Override
	public ItemReader<HourlyRowDataDto> getItemReader(FileType type) {
		FlatFileItemReader<HourlyRowDataDto> itemReader = new FlatFileItemReader<>();
		DefaultLineMapper<HourlyRowDataDto> lineMapper = getLineMapper(type);
		itemReader.setLinesToSkip(1);
		itemReader.setLineMapper(lineMapper);
		return itemReader;
	}

	@Override
	public DefaultLineMapper<HourlyRowDataDto> getLineMapper(FileType type) {
		DefaultLineMapper<HourlyRowDataDto> lineMapper = new DefaultLineMapper<>();
		lineMapper.setLineTokenizer(new DelimitedLineTokenizer(type.getSeperator()));
		lineMapper.setFieldSetMapper(new RowDataMapper());
		return lineMapper;
	}
}
