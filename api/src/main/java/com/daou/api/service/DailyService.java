package com.daou.api.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.daou.api.dto.request.search.SearchConditionDto;
import com.daou.api.dto.response.DailyResponseDto;
import com.daou.api.repository.HourlyDataRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class DailyService {

	private final HourlyDataRepository repository;

	@Transactional(readOnly = true)
	public Page<DailyResponseDto.DailyNewUser> getDailyNewUser(SearchConditionDto condition, Pageable pageable) {
		return repository.findDailyNewUserWithConditions(condition, pageable);
	}

	@Transactional(readOnly = true)
	public Page<DailyResponseDto.DailyChurnUser> getDailyChurnUser(SearchConditionDto condition, Pageable pageable) {
		return repository.findDailyChurnUserWithConditions(condition, pageable);
	}

	@Transactional(readOnly = true)
	public Page<DailyResponseDto.DailyPayAmount> getDailyPayAmount(SearchConditionDto condition, Pageable pageable) {
		return repository.findDailyPayAmountWithConditions(condition, pageable);
	}

	@Transactional(readOnly = true)
	public Page<DailyResponseDto.DailyCost> getDailyCost(SearchConditionDto condition, Pageable pageable) {
		return repository.findDailyCostWithConditions(condition, pageable);
	}

	@Transactional(readOnly = true)
	public Page<DailyResponseDto.DailySalesAmount> getDailySalesAmount(SearchConditionDto condition,
		Pageable pageable) {
		return repository.findDailySalesAmountWithConditions(condition, pageable);
	}

}
