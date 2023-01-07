package com.daou.batch.reader;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;

import com.daou.batch.dto.HourlyRowDataDto;

public interface FileReader<T> {

	ItemReader<HourlyRowDataDto> readFiles(T t) throws Exception;

	ItemReader<HourlyRowDataDto> getItemReader(T t);

	DefaultLineMapper<HourlyRowDataDto> getLineMapper(T t);
}
