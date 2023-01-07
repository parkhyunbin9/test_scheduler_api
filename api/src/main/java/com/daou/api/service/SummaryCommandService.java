package com.daou.api.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daou.api.common.spec.CommonException;
import com.daou.api.common.spec.ExceptionCode;
import com.daou.api.dto.request.DailySummaryRequestDto;
import com.daou.api.dto.request.HourlySummaryRequestDto;
import com.daou.api.dto.response.SummaryResponseDto;
import com.daou.api.model.DailySummary;
import com.daou.api.model.HourlySummary;
import com.daou.api.repository.DailySummaryRepository;
import com.daou.api.repository.HourlySummaryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SummaryCommandService {

	private final HourlySummaryRepository hourlyRepository;
	private final DailySummaryRepository dailyRepository;

	public SummaryResponseDto.Hourly saveHourlySummary(HourlySummaryRequestDto.SaveRequest saveRequest) {
		Optional<HourlySummary> savedHour = hourlyRepository.findByHour(saveRequest.getHour());
		if (savedHour.isPresent()) {
			throw new CommonException(ExceptionCode.ALREADY_AGGREGATED_DATA);
		}

		return SummaryResponseDto.Hourly.fromEntity(hourlyRepository.save(saveRequest.toEntity()));
	}

	public SummaryResponseDto.Hourly updateHourlySummary(HourlySummaryRequestDto.UpdateRequest updateRequest) {
		HourlySummary originHourlySummary = hourlyRepository.findByHour(updateRequest.getHour())
			.orElseThrow(() -> new CommonException(ExceptionCode.NOT_FOUND));
		originHourlySummary.change(updateRequest.toEntity());

		return SummaryResponseDto.Hourly.fromEntity(originHourlySummary);
	}

	public void deleteHourlySummary(HourlySummaryRequestDto.DeleteRequest deleteRequest) {
		HourlySummary originHourlySummary = hourlyRepository.findByHour(deleteRequest.getHour())
			.orElseThrow(() -> new CommonException(ExceptionCode.NOT_FOUND));

		hourlyRepository.delete(originHourlySummary);
	}

	public SummaryResponseDto.Daily saveDailySummary(DailySummaryRequestDto.SaveRequest saveRequest) {
		Optional<DailySummary> savedDate = dailyRepository.findByDate(saveRequest.getDate());
		if (savedDate.isPresent()) {
			throw new CommonException(ExceptionCode.ALREADY_AGGREGATED_DATA);
		}

		return SummaryResponseDto.Daily.fromEntity(dailyRepository.save(saveRequest.toEntity()));
	}

	public SummaryResponseDto.Daily updateDailySummary(DailySummaryRequestDto.UpdateRequest updateRequest) {
		DailySummary originDailySummary = dailyRepository.findByDate(updateRequest.getDate())
			.orElseThrow(() -> new CommonException(ExceptionCode.NOT_FOUND));
		originDailySummary.change(updateRequest.toEntity());

		return SummaryResponseDto.Daily.fromEntity(originDailySummary);
	}

	public void deleteDailySummary(DailySummaryRequestDto.DeleteRequest deleteRequest) {
		DailySummary originDailySummary = dailyRepository.findByDate(deleteRequest.getDate())
			.orElseThrow(() -> new CommonException(ExceptionCode.NOT_FOUND));

		dailyRepository.delete(originDailySummary);
	}
}