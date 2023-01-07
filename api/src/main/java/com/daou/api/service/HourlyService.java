package com.daou.api.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daou.api.dto.request.search.SearchConditionDto;
import com.daou.api.dto.response.HourlyResponseDto;
import com.daou.api.repository.HourlyDataRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class HourlyService {

	private final HourlyDataRepository repository;

	@Transactional(readOnly = true)
	public Page<HourlyResponseDto.HourlyNewUser> getHourlyNewUser(SearchConditionDto requestDto, Pageable pageable) {
		return repository.findHourlyNewUserWithConditions(requestDto, pageable);
	}

	@Transactional(readOnly = true)
	public Page<HourlyResponseDto.HourlyChurnUser> getHourlyChurnUser(SearchConditionDto requestDto,
		Pageable pageable) {
		return repository.findHourlyChurnUserWithConditions(requestDto, pageable);
	}

	@Transactional(readOnly = true)
	public Page<HourlyResponseDto.HourlyPayAmount> getHourlyPayAmount(SearchConditionDto requestDto,
		Pageable pageable) {
		return repository.findHourlyPayAmountWithConditions(requestDto, pageable);
	}

	@Transactional(readOnly = true)
	public Page<HourlyResponseDto.HourlyCost> getHourlyCost(SearchConditionDto requestDto, Pageable pageable) {
		return repository.findHourlyCostWithConditions(requestDto, pageable);
	}

	@Transactional(readOnly = true)
	public Page<HourlyResponseDto.HourlySalesAmount> getHourlySalesAmount(SearchConditionDto requestDto,
		Pageable pageable) {
		return repository.findHourlySalesAmountWithConditions(requestDto, pageable);
	}
}
