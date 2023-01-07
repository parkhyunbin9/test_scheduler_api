package com.daou.batch.reader.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;

import lombok.extern.slf4j.Slf4j;
import com.daou.batch.dto.HourlyRowDataDto;

@Slf4j
@Component
public class RowDataMapper implements FieldSetMapper<HourlyRowDataDto> {

	@Override
	public HourlyRowDataDto mapFieldSet(FieldSet fieldSet) throws BindException {

		String[] dateAndHour = fieldSet.readString(0).split(" ");
		return HourlyRowDataDto.builder()
			.date(LocalDate.parse(dateAndHour[0], DateTimeFormatter.ISO_DATE))
			.hour(Integer.parseInt(dateAndHour[1]))
			.newUser(fieldSet.readLong(1))
			.churnUser(fieldSet.readLong(2))
			.payAmount(fieldSet.readBigDecimal(3))
			.cost(fieldSet.readBigDecimal(4))
			.salesAmount(fieldSet.readBigDecimal(5))
			.build();

	}
}
