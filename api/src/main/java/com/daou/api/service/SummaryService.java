package com.daou.api.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daou.api.dto.request.search.SearchConditionDto;
import com.daou.api.dto.response.SummaryResponseDto;
import com.daou.api.repository.DailySummaryRepository;
import com.daou.api.repository.HourlySummaryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SummaryService {

	private final HourlySummaryRepository hourlyRepository;
	private final DailySummaryRepository dailyRepository;

	@Transactional(readOnly = true)
	public List<SummaryResponseDto.Hourly> getHourlySummary(SearchConditionDto condition) {
		return hourlyRepository.findHourlySummaryWithCondition(condition);
	}

	@Transactional(readOnly = true)
	public List<SummaryResponseDto.Daily> getDailySummary(SearchConditionDto condition) {
		return dailyRepository.findDailySummaryWithCondition(condition);
	}

}
