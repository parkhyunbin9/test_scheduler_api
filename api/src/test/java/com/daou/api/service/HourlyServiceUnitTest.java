package com.daou.api.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
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
import com.daou.api.dto.response.HourlyResponseDto;
import com.daou.api.repository.HourlyDataRepository;

@ExtendWith(MockitoExtension.class)
class HourlyServiceUnitTest {

	@InjectMocks
	HourlyService hourlyService;

	@Mock
	HourlyDataRepository hourlyDataRepository;

	@DisplayName("시간별 신규 유저 조회 - Unit")
	@Test
	void getHourlyNewUserUnitTest() {

		// given
		HourlyResponseDto.HourlyNewUser expect = new HourlyResponseDto.HourlyNewUser(0, 10L);

		when(hourlyDataRepository.findHourlyNewUserWithConditions(any(SearchConditionDto.class), any(Pageable.class)))
			.thenReturn(new PageImpl<>(List.of(expect)));

		// when
		Page<HourlyResponseDto.HourlyNewUser> findData = hourlyService.getHourlyNewUser(new SearchConditionDto(0),
			Pageable.ofSize(1));

		// then
		assertThat(findData.getContent().get(0)).usingRecursiveComparison().isEqualTo(expect);
		assertThat(findData.getTotalElements()).isEqualTo(1);

	}

	@DisplayName("시간별 이탈 유저 조회 - Unit")
	@Test
	void getHourlyChurnUserUnitTest() {

		// given
		HourlyResponseDto.HourlyChurnUser expect = new HourlyResponseDto.HourlyChurnUser(0, 10L);

		when(hourlyDataRepository.findHourlyChurnUserWithConditions(any(SearchConditionDto.class), any(Pageable.class)))
			.thenReturn(new PageImpl<>(List.of(expect)));

		// when
		Page<HourlyResponseDto.HourlyChurnUser> findData = hourlyService.getHourlyChurnUser(new SearchConditionDto(0),
			Pageable.ofSize(1));

		// then
		assertThat(findData.getContent().get(0)).usingRecursiveComparison().isEqualTo(expect);
		assertThat(findData.getTotalElements()).isEqualTo(1);

	}

	@DisplayName("시간별 매출 조회 - Unit")
	@Test
	void getHourlyPayAmountUnitTest() {

		// given
		HourlyResponseDto.HourlyPayAmount expect = new HourlyResponseDto.HourlyPayAmount(0, BigDecimal.ONE);

		when(hourlyDataRepository.findHourlyPayAmountWithConditions(any(SearchConditionDto.class), any(Pageable.class)))
			.thenReturn(new PageImpl<>(List.of(expect)));

		// when
		Page<HourlyResponseDto.HourlyPayAmount> findData = hourlyService.getHourlyPayAmount(new SearchConditionDto(0),
			Pageable.ofSize(1));

		// then
		assertThat(findData.getContent().get(0)).usingRecursiveComparison().isEqualTo(expect);
		assertThat(findData.getTotalElements()).isEqualTo(1);

	}

	@DisplayName("시간별 비용 조회 - Unit")
	@Test
	void getHourlyCostUnitTest() {

		// given
		HourlyResponseDto.HourlyCost expect = new HourlyResponseDto.HourlyCost(0, BigDecimal.ONE);

		when(hourlyDataRepository.findHourlyCostWithConditions(any(SearchConditionDto.class), any(Pageable.class)))
			.thenReturn(new PageImpl<>(List.of(expect)));

		// when
		Page<HourlyResponseDto.HourlyCost> findData = hourlyService.getHourlyCost(new SearchConditionDto(0),
			Pageable.ofSize(1));

		// then
		assertThat(findData.getContent().get(0)).usingRecursiveComparison().isEqualTo(expect);
		assertThat(findData.getTotalElements()).isEqualTo(1);

	}

	@DisplayName("시간별 판매 금액 조회 - Unit")
	@Test
	void getHourlySalesAmountUnitTest() {

		// given
		HourlyResponseDto.HourlySalesAmount expect = new HourlyResponseDto.HourlySalesAmount(0, BigDecimal.ONE);

		when(hourlyDataRepository.findHourlySalesAmountWithConditions(any(SearchConditionDto.class),
			any(Pageable.class)))
			.thenReturn(new PageImpl<>(List.of(expect)));

		// when
		Page<HourlyResponseDto.HourlySalesAmount> findData = hourlyService.getHourlySalesAmount(
			new SearchConditionDto(0),
			Pageable.ofSize(1));

		// then
		assertThat(findData.getContent().get(0)).usingRecursiveComparison().isEqualTo(expect);
		assertThat(findData.getTotalElements()).isEqualTo(1);

	}

}