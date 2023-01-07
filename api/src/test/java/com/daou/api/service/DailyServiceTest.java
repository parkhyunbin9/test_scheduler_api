package com.daou.api.service;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.daou.api.dto.request.search.SearchConditionDto;
import com.daou.api.dto.response.DailyResponseDto;
import com.daou.api.model.HourlyData;
import com.daou.api.repository.HourlyDataRepository;

@SpringBootTest
@Transactional
class DailyServiceTest {
	final LocalDate TEST_DATE = LocalDate.of(2020, 1, 1);
	final LocalDate invalidDate = LocalDate.of(2020, 2, 1);
	final Integer TEST_HOUR = 3;
	final LocalDate DATE_START = LocalDate.of(2022, 1, 1);
	final LocalDate DATE_END = LocalDate.of(2022, 1, 10);
	final Integer HOUR_START = 0;
	final Integer HOUR_END = 10;
	@Autowired
	HourlyDataRepository repository;
	Long newUserNum = 10L;
	Long churnUserNum = 20L;
	BigDecimal payAmountNum = BigDecimal.valueOf(30L);
	BigDecimal costNum = BigDecimal.valueOf(40L);
	BigDecimal salesAmountNum = BigDecimal.valueOf(50L);

	List<HourlyData> rangeData;

	@BeforeEach
	public void init() {
		List<HourlyData> testData = IntStream.range(0, 24).mapToObj(i ->
			HourlyData.builder()
				.date(TEST_DATE)
				.hour(i)
				.newUser(newUserNum).churnUser(churnUserNum)
				.payAmount(payAmountNum).cost(costNum)
				.salesAmount(salesAmountNum)
				.build()
		).collect(Collectors.toList());
		repository.saveAll(testData);
		rangeData = IntStream.range(0, 23).mapToObj(i ->
			HourlyData.builder()
				.date(LocalDate.of(2022, 1, i + 1))
				.hour(20)
				.newUser(1L)
				.churnUser(3L)
				.payAmount(BigDecimal.valueOf(100L))
				.cost(BigDecimal.valueOf(100L))
				.salesAmount(BigDecimal.valueOf(100L))
				.build()
		).collect(Collectors.toList());
		repository.saveAll(rangeData);
	}

	@Nested
	@DisplayName("일별 데이터 시간 기준 페이징 조회")
	class SearchDailyDataByDate {

		@DisplayName("신규유저 조회")
		@Test
		void searchNewUserWithPageTest() {

			// given
			SearchConditionDto cond = new SearchConditionDto(TEST_DATE);
			SearchConditionDto noDateCond = new SearchConditionDto(invalidDate);
			Pageable pageable = Pageable.ofSize(24).withPage(0);
			Pageable halfPage = Pageable.ofSize(12);

			// when
			Page<DailyResponseDto.DailyNewUser> result = repository.findDailyNewUserWithConditions(cond, pageable);
			Page<DailyResponseDto.DailyNewUser> invalidDateResult = repository.findDailyNewUserWithConditions(
				noDateCond,
				pageable);
			Page<DailyResponseDto.DailyNewUser> halfResult = repository.findDailyNewUserWithConditions(cond, halfPage);

			// then
			assertThat(result.getTotalElements()).isEqualTo(24);
			assertThat(
				result.stream()
					.mapToLong(DailyResponseDto.DailyNewUser::getNewUser)
					.reduce(Math::addExact)
					.orElseThrow())
				.isEqualTo(newUserNum * 24);
			assertThat(invalidDateResult.getTotalElements()).isEqualTo(0);
			assertThat(result.getTotalPages()).isEqualTo(1);
			assertThat(halfResult.getPageable().next().getPageSize()).isEqualTo(12);
			assertThat(halfResult.getTotalPages()).isEqualTo(2);

		}

		@DisplayName("이탈유저 조회")
		@Test
		void searchChurnUserWithPageTest() {

			// given
			SearchConditionDto cond = new SearchConditionDto(TEST_DATE);
			SearchConditionDto noDateCond = new SearchConditionDto(invalidDate);
			Pageable pageable = Pageable.ofSize(24).withPage(0);
			Pageable halfPage = Pageable.ofSize(12);

			// when
			Page<DailyResponseDto.DailyChurnUser> result = repository.findDailyChurnUserWithConditions(cond, pageable);
			Page<DailyResponseDto.DailyChurnUser> invalidDateResult = repository.findDailyChurnUserWithConditions(
				noDateCond,
				pageable);
			Page<DailyResponseDto.DailyChurnUser> halfResult = repository.findDailyChurnUserWithConditions(cond,
				halfPage);

			// then
			assertThat(result.getTotalElements()).isEqualTo(24);
			assertThat(
				result.stream()
					.mapToLong(DailyResponseDto.DailyChurnUser::getChurnUser)
					.reduce(Math::addExact)
					.orElseThrow())
				.isEqualTo(churnUserNum * 24);
			assertThat(invalidDateResult.getTotalElements()).isEqualTo(0);
			assertThat(result.getTotalPages()).isEqualTo(1);
			assertThat(halfResult.getPageable().next().getPageSize()).isEqualTo(12);
			assertThat(halfResult.getTotalPages()).isEqualTo(2);

		}

		@DisplayName("매출 조회")
		@Test
		void searchPayAmountWithPageTest() {

			// given
			SearchConditionDto cond = new SearchConditionDto(TEST_DATE);
			SearchConditionDto noDateCond = new SearchConditionDto(invalidDate);
			Pageable pageable = Pageable.ofSize(24).withPage(0);
			Pageable halfPage = Pageable.ofSize(12);

			// when
			Page<DailyResponseDto.DailyPayAmount> result = repository.findDailyPayAmountWithConditions(cond, pageable);
			Page<DailyResponseDto.DailyPayAmount> invalidDateResult = repository.findDailyPayAmountWithConditions(
				noDateCond, pageable);
			Page<DailyResponseDto.DailyPayAmount> halfResult = repository.findDailyPayAmountWithConditions(cond,
				halfPage);

			// then
			assertThat(result.getTotalElements()).isEqualTo(24);
			assertThat(
				result.stream()
					.map(DailyResponseDto.DailyPayAmount::getPayAmount)
					.reduce(BigDecimal::add)
					.orElseThrow())
				.isEqualByComparingTo(payAmountNum.multiply(BigDecimal.valueOf(24L)));
			assertThat(invalidDateResult.getTotalElements()).isEqualTo(0);
			assertThat(result.getTotalPages()).isEqualTo(1);
			assertThat(halfResult.getPageable().next().getPageSize()).isEqualTo(12);
			assertThat(halfResult.getTotalPages()).isEqualTo(2);

		}

		@DisplayName("비용 조회")
		@Test
		void searchCostWithPageTest() {

			// given
			SearchConditionDto cond = new SearchConditionDto(TEST_DATE);
			SearchConditionDto noDateCond = new SearchConditionDto(invalidDate);
			Pageable pageable = Pageable.ofSize(24).withPage(0);
			Pageable halfPage = Pageable.ofSize(12);

			// when
			Page<DailyResponseDto.DailyCost> result = repository.findDailyCostWithConditions(cond, pageable);
			Page<DailyResponseDto.DailyCost> invalidDateResult = repository.findDailyCostWithConditions(noDateCond,
				pageable);
			Page<DailyResponseDto.DailyCost> halfResult = repository.findDailyCostWithConditions(cond, halfPage);

			// then
			assertThat(invalidDateResult.getTotalElements()).isEqualTo(0);
			assertThat(result.getTotalElements()).isEqualTo(24);
			assertThat(result.stream().map(DailyResponseDto.DailyCost::getCost).reduce(BigDecimal::add).orElseThrow())
				.isEqualByComparingTo(costNum.multiply(BigDecimal.valueOf(24L)));
			assertThat(result.getTotalPages()).isEqualTo(1);
			assertThat(halfResult.getPageable().next().getPageSize()).isEqualTo(12);
			assertThat(halfResult.getTotalPages()).isEqualTo(2);
		}

		@DisplayName("판매금액 조회")
		@Test
		void searchSalesAmountWithPageTest() {

			// given
			SearchConditionDto cond = new SearchConditionDto(TEST_DATE);
			SearchConditionDto noDateCond = new SearchConditionDto(invalidDate);
			Pageable pageable = Pageable.ofSize(24).withPage(0);
			Pageable halfPage = Pageable.ofSize(12);

			// when
			Page<DailyResponseDto.DailySalesAmount> result = repository.findDailySalesAmountWithConditions(cond,
				pageable);
			Page<DailyResponseDto.DailySalesAmount> invalidDateResult = repository.findDailySalesAmountWithConditions(
				noDateCond, pageable);
			Page<DailyResponseDto.DailySalesAmount> halfResult = repository.findDailySalesAmountWithConditions(cond,
				halfPage);

			// then
			assertThat(result.getTotalElements()).isEqualTo(24);
			assertThat(result.stream()
				.map(DailyResponseDto.DailySalesAmount::getSalesAmount)
				.reduce(BigDecimal::add)
				.orElseThrow())
				.isEqualByComparingTo(salesAmountNum.multiply(BigDecimal.valueOf(24L)));
			assertThat(invalidDateResult.getTotalElements()).isEqualTo(0);
			assertThat(result.getTotalPages()).isEqualTo(1);
			assertThat(halfResult.getPageable().next().getPageSize()).isEqualTo(12);
			assertThat(halfResult.getTotalPages()).isEqualTo(2);
		}
	}

	@Nested
	@DisplayName("일별 데이터 날짜범위 조회")
	class SearchDailyDataByDateRange {

		@DisplayName("신규유저 조회")
		@Test
		void searchNewUserByDateRange() {

			// given
			SearchConditionDto dateRangeCond = new SearchConditionDto(DATE_START, DATE_END);
			int rangeDayCount = DATE_END.getDayOfMonth() - DATE_START.getDayOfMonth() + 1;
			Pageable pageable = Pageable.ofSize(24).withPage(0);

			// when
			Page<DailyResponseDto.DailyNewUser> dateRangeResult = repository.findDailyNewUserWithConditions(
				dateRangeCond,
				pageable);

			// then
			assertThat(dateRangeResult.getTotalElements()).isEqualTo(rangeDayCount);
			assertThat(dateRangeResult).extracting("date").contains(DATE_START, DATE_END);

		}

		@DisplayName("이탈유저 조회")
		@Test
		void searchChurnUserByDateRange() {

			// given
			SearchConditionDto dateRangeCond = new SearchConditionDto(DATE_START, DATE_END);
			int rangeDayCount = DATE_END.getDayOfMonth() - DATE_START.getDayOfMonth() + 1;
			Pageable pageable = Pageable.ofSize(24).withPage(0);

			// when
			Page<DailyResponseDto.DailyChurnUser> dateRangeResult = repository.findDailyChurnUserWithConditions(
				dateRangeCond,
				pageable);

			// then
			assertThat(dateRangeResult.getTotalElements()).isEqualTo(rangeDayCount);
			assertThat(dateRangeResult).extracting("date").contains(DATE_START, DATE_END);
		}

		@DisplayName("매출 조회")
		@Test
		void searchPayAmountByDateRange() {

			// given
			SearchConditionDto dateRangeCond = new SearchConditionDto(DATE_START, DATE_END);
			int rangeDayCount = DATE_END.getDayOfMonth() - DATE_START.getDayOfMonth() + 1;
			Pageable pageable = Pageable.ofSize(24).withPage(0);

			// when
			Page<DailyResponseDto.DailyPayAmount> dateRangeResult = repository.findDailyPayAmountWithConditions(
				dateRangeCond, pageable);

			// then
			assertThat(dateRangeResult.getTotalElements()).isEqualTo(rangeDayCount);
			assertThat(dateRangeResult).extracting("date").contains(DATE_START, DATE_END);
		}

		@DisplayName("비용 조회")
		@Test
		void searchCostByDateRange() {

			// given
			SearchConditionDto dateRangeCond = new SearchConditionDto(DATE_START, DATE_END);
			int rangeDayCount = DATE_END.getDayOfMonth() - DATE_START.getDayOfMonth() + 1;
			Pageable pageable = Pageable.ofSize(24).withPage(0);

			// when
			Page<DailyResponseDto.DailyCost> dateRangeResult = repository.findDailyCostWithConditions(dateRangeCond,
				pageable);

			// then
			assertThat(dateRangeResult.getTotalElements()).isEqualTo(rangeDayCount);
			assertThat(dateRangeResult).extracting("date").contains(DATE_START, DATE_END);
		}

		@DisplayName("판매금액 조회")
		@Test
		void searchSalesAmountByDateRange() {

			// given
			SearchConditionDto dateRangeCond = new SearchConditionDto(DATE_START, DATE_END);
			int rangeDayCount = DATE_END.getDayOfMonth() - DATE_START.getDayOfMonth() + 1;
			Pageable pageable = Pageable.ofSize(24).withPage(0);

			// when
			Page<DailyResponseDto.DailySalesAmount> dateRangeResult = repository.findDailySalesAmountWithConditions(
				dateRangeCond, pageable);

			// then
			assertThat(dateRangeResult.getTotalElements()).isEqualTo(rangeDayCount);
			assertThat(dateRangeResult).extracting("date").contains(DATE_START, DATE_END);
		}
	}

	@Nested
	@DisplayName("일별 데이터 시간 조회")
	class SearchDailyDataByHour {

		@DisplayName("신규유저 조회")
		@Test
		void searchNewUserByHour() {

			// given
			SearchConditionDto hourCond = new SearchConditionDto(TEST_HOUR);
			Pageable pageable = Pageable.ofSize(24).withPage(0);

			// when
			Page<DailyResponseDto.DailyNewUser> hourResult = repository.findDailyNewUserWithConditions(hourCond,
				pageable);

			// then
			assertThat(hourResult.getTotalElements()).isEqualTo(1);
		}

		@DisplayName("이탈유저 조회")
		@Test
		void searchChurnUserByHour() {

			// given
			SearchConditionDto hourCond = new SearchConditionDto(TEST_HOUR);
			Pageable pageable = Pageable.ofSize(24).withPage(0);

			// when
			Page<DailyResponseDto.DailyChurnUser> hourResult = repository.findDailyChurnUserWithConditions(hourCond,
				pageable);

			// then
			assertThat(hourResult.getTotalElements()).isEqualTo(1);
		}

		@DisplayName("매출 조회")
		@Test
		void searchPayAmountsByHour() {

			// given
			SearchConditionDto hourCond = new SearchConditionDto(TEST_HOUR);
			Pageable pageable = Pageable.ofSize(24).withPage(0);

			// when
			Page<DailyResponseDto.DailyPayAmount> hourResult = repository.findDailyPayAmountWithConditions(hourCond,
				pageable);

			// then
			assertThat(hourResult.getTotalElements()).isEqualTo(1);
		}

		@DisplayName("비용 조회")
		@Test
		void searchCostByHour() {

			// given
			SearchConditionDto hourCond = new SearchConditionDto(TEST_HOUR);
			Pageable pageable = Pageable.ofSize(24).withPage(0);

			// when
			Page<DailyResponseDto.DailyCost> hourResult = repository.findDailyCostWithConditions(hourCond, pageable);

			// then
			assertThat(hourResult.getTotalElements()).isEqualTo(1);
		}

		@DisplayName("판매금액 조회")
		@Test
		void searchSalesAmountByHour() {

			// given
			SearchConditionDto hourCond = new SearchConditionDto(TEST_HOUR);
			Pageable pageable = Pageable.ofSize(24).withPage(0);

			// when
			Page<DailyResponseDto.DailySalesAmount> hourResult = repository.findDailySalesAmountWithConditions(hourCond,
				pageable);

			// then
			assertThat(hourResult.getTotalElements()).isEqualTo(1);
		}
	}

	@Nested
	@DisplayName("일별 데이터 시간범위 조회")
	class SearchDailyDataByHourRange {

		@DisplayName("신규유저 조회")
		@Test
		void searchNewUserByHourRange() {

			// given
			SearchConditionDto hourRangeCond = new SearchConditionDto(HOUR_START, HOUR_END);
			int rangeHourCount = HOUR_END - HOUR_START + 1;
			Pageable pageable = Pageable.ofSize(24).withPage(0);

			// when
			Page<DailyResponseDto.DailyNewUser> HourRangeResult = repository.findDailyNewUserWithConditions(
				hourRangeCond,
				pageable);

			// then
			assertThat(HourRangeResult.getTotalElements()).isEqualTo(rangeHourCount);
		}

		@DisplayName("이탈유저 조회")
		@Test
		void searchChurnUserByHourRange() {

			// given
			SearchConditionDto hourRangeCond = new SearchConditionDto(HOUR_START, HOUR_END);
			int rangeHourCount = HOUR_END - HOUR_START + 1;
			Pageable pageable = Pageable.ofSize(24).withPage(0);

			// when
			Page<DailyResponseDto.DailyChurnUser> HourRangeResult = repository.findDailyChurnUserWithConditions(
				hourRangeCond,
				pageable);

			// then
			assertThat(HourRangeResult.getTotalElements()).isEqualTo(rangeHourCount);

		}

		@DisplayName("매출 조회")
		@Test
		void searchPayAmountByHourRange() {

			// given
			SearchConditionDto hourRangeCond = new SearchConditionDto(HOUR_START, HOUR_END);
			int rangeHourCount = HOUR_END - HOUR_START + 1;
			Pageable pageable = Pageable.ofSize(24).withPage(0);

			// when
			Page<DailyResponseDto.DailyPayAmount> HourRangeResult = repository.findDailyPayAmountWithConditions(
				hourRangeCond, pageable);

			// then
			assertThat(HourRangeResult.getTotalElements()).isEqualTo(rangeHourCount);
		}

		@DisplayName("비용 조회")
		@Test
		void searchCostByHourRange() {

			// given
			SearchConditionDto hourRangeCond = new SearchConditionDto(HOUR_START, HOUR_END);
			int rangeHourCount = HOUR_END - HOUR_START + 1;
			Pageable pageable = Pageable.ofSize(24).withPage(0);

			// when
			Page<DailyResponseDto.DailyCost> HourRangeResult = repository.findDailyCostWithConditions(hourRangeCond,
				pageable);

			// then
			assertThat(HourRangeResult.getTotalElements()).isEqualTo(rangeHourCount);
		}

		@DisplayName("판매금액 조회")
		@Test
		void searchSalesAmountByHourRange() {

			// given
			SearchConditionDto hourRangeCond = new SearchConditionDto(HOUR_START, HOUR_END);
			int rangeHourCount = HOUR_END - HOUR_START + 1;
			Pageable pageable = Pageable.ofSize(24).withPage(0);

			// when
			Page<DailyResponseDto.DailySalesAmount> HourRangeResult = repository.findDailySalesAmountWithConditions(
				hourRangeCond, pageable);

			// then
			assertThat(HourRangeResult.getTotalElements()).isEqualTo(rangeHourCount);
		}
	}

}