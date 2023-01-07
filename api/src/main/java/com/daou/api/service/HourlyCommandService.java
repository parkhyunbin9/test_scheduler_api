package com.daou.api.service;

import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.daou.api.common.spec.CommonException;
import com.daou.api.common.spec.ExceptionCode;
import com.daou.api.dto.request.HourlyInfoRequestDto;
import com.daou.api.dto.response.HourlyDataResponseDto;
import com.daou.api.model.HourlyData;
import com.daou.api.repository.HourlyDataRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class HourlyCommandService {

	private final HourlyDataRepository repository;

	public HourlyDataResponseDto.SaveAndUpdateResponse saveHourlyData(HourlyInfoRequestDto.SaveRequest requestDto) {
		return HourlyDataResponseDto.SaveAndUpdateResponse.fromEntity(repository.save(requestDto.toEntity()));
	}

	public HourlyDataResponseDto.SaveAndUpdateResponse updateHourlyData(
		HourlyInfoRequestDto.UpdateRequest requestDto) {
		HourlyData originHourlyData = repository.findByDateAndHour(requestDto.getDate(), requestDto.getHour())
			.orElseThrow(() -> new CommonException(ExceptionCode.NOT_FOUND));
		originHourlyData.change(requestDto.toEntity());

		return HourlyDataResponseDto.SaveAndUpdateResponse.fromEntity(originHourlyData);
	}

	public void deleteHourlyData(HourlyInfoRequestDto.DeleteRequest requestDto) {
		HourlyData originHourlyData = repository.findByDateAndHour(requestDto.getDate(), requestDto.getHour())
			.orElseThrow(() -> new CommonException(ExceptionCode.NOT_FOUND));
		repository.delete(originHourlyData);
	}

}
