package com.daou.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.daou.api.dto.request.search.SearchConditionDto;
import com.daou.api.dto.response.DailyResponseDto;
import com.daou.api.dto.response.HourlyResponseDto;

public interface HourlyDataRepositoryCustom {

	Page<HourlyResponseDto.HourlyNewUser> findHourlyNewUserWithConditions(SearchConditionDto condition,
		Pageable pageable);

	Page<HourlyResponseDto.HourlyChurnUser> findHourlyChurnUserWithConditions(SearchConditionDto condition,
		Pageable pageable);

	Page<HourlyResponseDto.HourlyPayAmount> findHourlyPayAmountWithConditions(SearchConditionDto condition,
		Pageable pageable);

	Page<HourlyResponseDto.HourlyCost> findHourlyCostWithConditions(SearchConditionDto condition, Pageable pageable);

	Page<HourlyResponseDto.HourlySalesAmount> findHourlySalesAmountWithConditions(SearchConditionDto condition,
		Pageable pageable);

	Page<DailyResponseDto.DailyNewUser> findDailyNewUserWithConditions(SearchConditionDto condition, Pageable pageable);

	Page<DailyResponseDto.DailyChurnUser> findDailyChurnUserWithConditions(SearchConditionDto condition,
		Pageable pageable);

	Page<DailyResponseDto.DailyPayAmount> findDailyPayAmountWithConditions(SearchConditionDto condition,
		Pageable pageable);

	Page<DailyResponseDto.DailyCost> findDailyCostWithConditions(SearchConditionDto condition, Pageable pageable);

	Page<DailyResponseDto.DailySalesAmount> findDailySalesAmountWithConditions(SearchConditionDto condition,
		Pageable pageable);

}
