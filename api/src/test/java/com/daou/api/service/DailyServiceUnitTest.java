package com.daou.api.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.daou.api.dto.request.search.SearchConditionDto;
import com.daou.api.dto.response.DailyResponseDto;
import com.daou.api.repository.HourlyDataRepository;

@ExtendWith(MockitoExtension.class)
class DailyServiceUnitTest {

	@InjectMocks
	DailyService dailyService;

	@Mock
	HourlyDataRepository hourlyDataRepository;

	@DisplayName("일별 신규 유저 조회 - Unit")
	@Test
	void getDailyNewUserUnitTest() {

		// given
		DailyResponseDto.DailyNewUser expect = new DailyResponseDto.DailyNewUser(LocalDate.of(2022, 01, 01), 10L);

		when(hourlyDataRepository.findDailyNewUserWithConditions(any(SearchConditionDto.class), any(Pageable.class)))
			.thenReturn(new PageImpl<>(List.of(expect)));


		// when
		Page<DailyResponseDto.DailyNewUser> findData = dailyService.getDailyNewUser(new SearchConditionDto(0),
			Pageable.ofSize(1));

		// then
		assertThat(findData.getContent().get(0)).usingRecursiveComparison().isEqualTo(expect);
		assertThat(findData.getTotalElements()).isEqualTo(1);

	}
	@DisplayName("일별 이탈 유저 조회 - Unit")
	@Test
	void getDailyChurnUserUnitTest() {

		// given
		DailyResponseDto.DailyChurnUser expect = new DailyResponseDto.DailyChurnUser(LocalDate.of(2022, 01, 01), 10L);

		when(hourlyDataRepository.findDailyChurnUserWithConditions(any(SearchConditionDto.class), any(Pageable.class)))
			.thenReturn(new PageImpl<>(List.of(expect)));


		// when
		Page<DailyResponseDto.DailyChurnUser> findData = dailyService.getDailyChurnUser(new SearchConditionDto(0),
			Pageable.ofSize(1));

		// then
		assertThat(findData.getContent().get(0)).usingRecursiveComparison().isEqualTo(expect);
		assertThat(findData.getTotalElements()).isEqualTo(1);

	}

	@DisplayName("일별 매출 조회 - Unit")
	@Test
	void getDailyPayAmountUnitTest() {

		// given
		DailyResponseDto.DailyPayAmount expect = new DailyResponseDto.DailyPayAmount(LocalDate.of(2022,01,01), BigDecimal.ONE);

		when(hourlyDataRepository.findDailyPayAmountWithConditions(any(SearchConditionDto.class), any(Pageable.class)))
			.thenReturn(new PageImpl<>(List.of(expect)));


		// when
		Page<DailyResponseDto.DailyPayAmount> findData = dailyService.getDailyPayAmount(new SearchConditionDto(0),
			Pageable.ofSize(1));

		// then
		assertThat(findData.getContent().get(0)).usingRecursiveComparison().isEqualTo(expect);
		assertThat(findData.getTotalElements()).isEqualTo(1);

	}

	@DisplayName("일별 비용 조회 - Unit")
	@Test
	void getDailyCostUnitTest() {

		// given
		DailyResponseDto.DailyCost expect = new DailyResponseDto.DailyCost(LocalDate.of(2022,01,01), BigDecimal.ONE);

		when(hourlyDataRepository.findDailyCostWithConditions(any(SearchConditionDto.class), any(Pageable.class)))
			.thenReturn(new PageImpl<>(List.of(expect)));


		// when
		Page<DailyResponseDto.DailyCost> findData = dailyService.getDailyCost(new SearchConditionDto(0),
			Pageable.ofSize(1));

		// then
		assertThat(findData.getContent().get(0)).usingRecursiveComparison().isEqualTo(expect);
		assertThat(findData.getTotalElements()).isEqualTo(1);

	}

	@DisplayName("일별 판매 금액 조회 - Unit")
	@Test
	void getDailySalesAmountUnitTest() {

		// given
		DailyResponseDto.DailySalesAmount expect = new DailyResponseDto.DailySalesAmount(LocalDate.of(2022,01,01), BigDecimal.ONE);

		when(hourlyDataRepository.findDailySalesAmountWithConditions(any(SearchConditionDto.class), any(Pageable.class)))
			.thenReturn(new PageImpl<>(List.of(expect)));


		// when
		Page<DailyResponseDto.DailySalesAmount> findData = dailyService.getDailySalesAmount(new SearchConditionDto(0),
			Pageable.ofSize(1));

		// then
		assertThat(findData.getContent().get(0)).usingRecursiveComparison().isEqualTo(expect);
		assertThat(findData.getTotalElements()).isEqualTo(1);

	}


}