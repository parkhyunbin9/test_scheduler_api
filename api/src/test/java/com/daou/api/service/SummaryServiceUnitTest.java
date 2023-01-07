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

import com.daou.api.dto.request.search.SearchConditionDto;
import com.daou.api.dto.response.SummaryResponseDto;
import com.daou.api.model.DailySummary;
import com.daou.api.model.HourlySummary;
import com.daou.api.repository.DailySummaryRepository;
import com.daou.api.repository.HourlySummaryRepository;

@ExtendWith(MockitoExtension.class)
class SummaryServiceUnitTest {

	@InjectMocks
	SummaryService summaryService;

	@Mock
	HourlySummaryRepository hourlySummaryRepository;

	@Mock
	DailySummaryRepository dailySummaryRepository;

	@DisplayName("시간별 요약 데이터 조회 - Unit")
	@Test
	void getHourlySummaryUnitTest() {

		// given
		SearchConditionDto searchCond = new SearchConditionDto(0);

		HourlySummary expectData = HourlySummary.builder()
			.hour(0)
			.sumNewUser(10L)
			.sumChurnUser(10L)
			.sumPayAmount(BigDecimal.valueOf(100L))
			.sumCost(BigDecimal.valueOf(100L))
			.sumSalesAmount(BigDecimal.valueOf(100L))
			.avgNewUser(10D)
			.avgChurnUser(10D)
			.avgPayAmount(10D)
			.avgCost(10D)
			.avgSalesAmount(10D)
			.maxNewUser(10L)
			.maxChurnUser(10L)
			.maxPayAmount(BigDecimal.valueOf(100L))
			.maxCost(BigDecimal.valueOf(100L))
			.maxSalesAmount(BigDecimal.valueOf(100L))
			.minNewUser(10L)
			.minChurnUser(10L)
			.minPayAmount(BigDecimal.valueOf(100L))
			.minCost(BigDecimal.valueOf(100L))
			.minSalesAmount(BigDecimal.valueOf(100L))
			.build();

		when(hourlySummaryRepository.findHourlySummaryWithCondition(any(SearchConditionDto.class)))
			.thenReturn(List.of(SummaryResponseDto.Hourly.fromEntity(expectData)));

		// when
		List<SummaryResponseDto.Hourly> findData = summaryService.getHourlySummary(searchCond);

		// then
		assertThat(findData.get(0)).usingRecursiveComparison()
			.isEqualTo(SummaryResponseDto.Hourly.fromEntity(expectData));
	}

	@DisplayName("날짜별 요약 데이터 조회 - Unit")
	@Test
	void getDailySummaryUnitTest() {

		// given
		SearchConditionDto searchCond = new SearchConditionDto(0);

		DailySummary expectData = DailySummary.builder()
			.date(LocalDate.of(2022, 01, 01))
			.sumNewUser(10L)
			.sumChurnUser(10L)
			.sumPayAmount(BigDecimal.valueOf(100L))
			.sumCost(BigDecimal.valueOf(100L))
			.sumSalesAmount(BigDecimal.valueOf(100L))
			.avgNewUser(10D)
			.avgChurnUser(10D)
			.avgPayAmount(10D)
			.avgCost(10D)
			.avgSalesAmount(10D)
			.maxNewUser(10L)
			.maxChurnUser(10L)
			.maxPayAmount(BigDecimal.valueOf(100L))
			.maxCost(BigDecimal.valueOf(100L))
			.maxSalesAmount(BigDecimal.valueOf(100L))
			.minNewUser(10L)
			.minChurnUser(10L)
			.minPayAmount(BigDecimal.valueOf(100L))
			.minCost(BigDecimal.valueOf(100L))
			.minSalesAmount(BigDecimal.valueOf(100L))
			.build();

		when(dailySummaryRepository.findDailySummaryWithCondition(any(SearchConditionDto.class)))
			.thenReturn(List.of(SummaryResponseDto.Daily.fromEntity(expectData)));

		// when
		List<SummaryResponseDto.Daily> findData = summaryService.getDailySummary(searchCond);

		// then
		assertThat(findData.get(0)).usingRecursiveComparison()
			.isEqualTo(SummaryResponseDto.Daily.fromEntity(expectData));
	}
}