package com.daou.batch.writer;

import static java.util.stream.Collectors.*;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import com.daou.batch.dto.HourlyRowDataDto;
import com.daou.batch.model.HourlyRowData;
import com.daou.batch.repository.HourlyRowDataRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RowDataWriter implements ItemWriter<HourlyRowDataDto> {

	private final HourlyRowDataRepository rowDataRepository;
	private final DataSource dataSource;

	@Override
	public void write(List<? extends HourlyRowDataDto> items) throws Exception {
		List<HourlyRowData> rowDatas = items.stream().map(HourlyRowDataDto::toEntity).collect(toList());
		rowDataRepository.saveAll(rowDatas);
	}
}
