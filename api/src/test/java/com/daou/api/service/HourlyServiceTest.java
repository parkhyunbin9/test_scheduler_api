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
import com.daou.api.dto.response.HourlyResponseDto;
import com.daou.api.model.HourlyData;
import com.daou.api.repository.HourlyDataRepository;

@SpringBootTest
@Transactional
class HourlyServiceTest {

	final LocalDate TEST_DATE = LocalDate.of(2020, 1, 1);
	final LocalDate INVALID_DATE = LocalDate.of(2020, 2, 1);
	final Integer TEST_HOUR = 3;
	final LocalDate DATE_START = LocalDate.of(2022, 1, 1);
	final LocalDate DATE_END = LocalDate.of(2022, 1, 10);
	final Integer HOUR_START = 0;
	final Integer HOUR_END = 10;
	@Autowired
	HourlyService service;
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
	@DisplayName("시간별 데이터 시간 조회")
	class SearchHourlyDataByHour {

		@DisplayName("신규유저 조회")
		@Test
		void searchNewUserByHour() {

			// given
			SearchConditionDto hourCond = new SearchConditionDto(TEST_HOUR);
			Pageable pageable = Pageable.ofSize(24).withPage(0);

			// when
			Page<HourlyResponseDto.HourlyNewUser> hourResult = service.getHourlyNewUser(hourCond, pageable);

			// then
			assertThat(hourResult.getTotalElements()).isEqualTo(1);
			assertThat(hourResult.getContent().get(0).getHour()).isEqualTo(TEST_HOUR);
		}

		@DisplayName("이탈유저 조회")
		@Test
		void searchChurnUserByHour() {

			// given
			SearchConditionDto hourCond = new SearchConditionDto(TEST_HOUR);
			Pageable pageable = Pageable.ofSize(24).withPage(0);

			// when
			Page<HourlyResponseDto.HourlyChurnUser> hourResult = service.getHourlyChurnUser(hourCond, pageable);

			// then
			assertThat(hourResult.getTotalElements()).isEqualTo(1);
			assertThat(hourResult.getContent().get(0).getHour()).isEqualTo(TEST_HOUR);
		}

		@DisplayName("매출 조회")
		@Test
		void searchPayAmountByHour() {

			// given
			SearchConditionDto hourCond = new SearchConditionDto(TEST_HOUR);
			Pageable pageable = Pageable.ofSize(24).withPage(0);

			// when
			Page<HourlyResponseDto.HourlyPayAmount> hourResult = service.getHourlyPayAmount(hourCond, pageable);

			// then
			assertThat(hourResult.getTotalElements()).isEqualTo(1);
			assertThat(hourResult.getContent().get(0).getHour()).isEqualTo(TEST_HOUR);

		}

		@DisplayName("비용 조회")
		@Test
		void searchCostByHour() {

			// given
			SearchConditionDto hourCond = new SearchConditionDto(TEST_HOUR);
			Pageable pageable = Pageable.ofSize(24).withPage(0);

			// when
			Page<HourlyResponseDto.HourlyCost> hourResult = repository.findHourlyCostWithConditions(hourCond, pageable);

			// then
			assertThat(hourResult.getTotalElements()).isEqualTo(1);
			assertThat(hourResult.getContent().get(0).getHour()).isEqualTo(TEST_HOUR);

		}

		@DisplayName("판매금액 조회")
		@Test
		void searchSalesAmountByHour() {

			// given
			SearchConditionDto hourCond = new SearchConditionDto(TEST_HOUR);
			Pageable pageable = Pageable.ofSize(24).withPage(0);

			// when
			Page<HourlyResponseDto.HourlySalesAmount> hourResult = service.getHourlySalesAmount(hourCond, pageable);

			// then
			assertThat(hourResult.getTotalElements()).isEqualTo(1);
			assertThat(hourResult.getContent().get(0).getHour()).isEqualTo(TEST_HOUR);
		}

	}

	@Nested
	@DisplayName("시간별 데이터 시간범위 조회")
	class SearchHourlyDataByHourRange {

		@DisplayName("신규유저 조회")
		@Test
		void searchNewUserByHourRange() {

			// given
			SearchConditionDto hourRangeCond = new SearchConditionDto(HOUR_START, HOUR_END);
			int rangeHourCount = HOUR_END - HOUR_START + 1;
			Pageable pageable = Pageable.ofSize(24).withPage(0);

			// when
			Page<HourlyResponseDto.HourlyNewUser> HourRangeResult = service.getHourlyNewUser(hourRangeCond, pageable);

			// then
			assertThat(HourRangeResult.getTotalElements()).isEqualTo(rangeHourCount);
			assertThat(HourRangeResult).extracting("hour").contains(HOUR_START, HOUR_END);
		}

		@DisplayName("이탈유저 조회")
		@Test
		void searchChurnUserByHourRange() {

			// given
			SearchConditionDto hourRangeCond = new SearchConditionDto(HOUR_START, HOUR_END);
			int rangeHourCount = HOUR_END - HOUR_START + 1;
			Pageable pageable = Pageable.ofSize(24).withPage(0);

			// when
			Page<HourlyResponseDto.HourlyChurnUser> HourRangeResult = service.getHourlyChurnUser(hourRangeCond,
				pageable);

			//then
			assertThat(HourRangeResult.getTotalElements()).isEqualTo(rangeHourCount);
			assertThat(HourRangeResult).extracting("hour").contains(HOUR_START, HOUR_END);
		}

		@DisplayName("매출 조회")
		@Test
		void searchPayAmountByHourRange() {

			// given
			SearchConditionDto hourRangeCond = new SearchConditionDto(HOUR_START, HOUR_END);
			int rangeHourCount = HOUR_END - HOUR_START + 1;
			Pageable pageable = Pageable.ofSize(24).withPage(0);

			// when
			Page<HourlyResponseDto.HourlyPayAmount> HourRangeResult = service.getHourlyPayAmount(hourRangeCond,
				pageable);

			// then
			assertThat(HourRangeResult.getTotalElements()).isEqualTo(rangeHourCount);
			assertThat(HourRangeResult).extracting("hour").contains(HOUR_START, HOUR_END);
		}

		@DisplayName("비용 조회")
		@Test
		void searchCostByHourRange() {

			// given
			SearchConditionDto hourRangeCond = new SearchConditionDto(HOUR_START, HOUR_END);
			int rangeHourCount = HOUR_END - HOUR_START + 1;
			Pageable pageable = Pageable.ofSize(24).withPage(0);

			// when
			Page<HourlyResponseDto.HourlyCost> HourRangeResult = repository.findHourlyCostWithConditions(hourRangeCond,
				pageable);

			// then
			assertThat(HourRangeResult.getTotalElements()).isEqualTo(rangeHourCount);
			assertThat(HourRangeResult).extracting("hour").contains(HOUR_START, HOUR_END);
		}

		@DisplayName("판매금액 조회")
		@Test
		void searchSalesAmountByHourRange() {

			// given
			SearchConditionDto hourRangeCond = new SearchConditionDto(HOUR_START, HOUR_END);
			int rangeHourCount = HOUR_END - HOUR_START + 1;
			Pageable pageable = Pageable.ofSize(24).withPage(0);

			// when
			Page<HourlyResponseDto.HourlySalesAmount> HourRangeResult = service.getHourlySalesAmount(hourRangeCond,
				pageable);

			// then
			assertThat(HourRangeResult.getTotalElements()).isEqualTo(rangeHourCount);
			assertThat(HourRangeResult).extracting("hour").contains(HOUR_START, HOUR_END);
		}
	}

	@DisplayName("시간별 데이터 날짜기준 페이징 조회")
	@Nested
	class SearchHourlyDataByDate {

		@DisplayName("신규유저 조회")
		@Test
		void searchNewUserByDateWithPage() {

			// given
			SearchConditionDto cond = new SearchConditionDto(TEST_DATE);
			SearchConditionDto noDateCond = new SearchConditionDto(INVALID_DATE);
			Pageable pageable = Pageable.ofSize(24).withPage(0);
			Pageable halfPage = Pageable.ofSize(12);

			// when
			Page<HourlyResponseDto.HourlyNewUser> newUserResult = service.getHourlyNewUser(cond, pageable);
			Page<HourlyResponseDto.HourlyNewUser> invalidDateResult = service.getHourlyNewUser(noDateCond, pageable);
			Page<HourlyResponseDto.HourlyNewUser> halfResult = service.getHourlyNewUser(cond, halfPage);

			// then
			assertThat(newUserResult.getTotalElements()).isEqualTo(24);
			assertThat(newUserResult.stream()
				.mapToLong(HourlyResponseDto.HourlyNewUser::getNewUser)
				.reduce(Math::addExact)
				.orElseThrow())
				.isEqualTo(newUserNum * 24);
			assertThat(invalidDateResult.getTotalElements()).isEqualTo(0);
			assertThat(newUserResult.getTotalPages()).isEqualTo(1);
			assertThat(halfResult.getPageable().next().getPageSize()).isEqualTo(12);
			assertThat(halfResult.getTotalPages()).isEqualTo(2);
		}

		@DisplayName("이탈유저 조회")
		@Test
		void searchChurnUserByDateWithPage() {

			// given
			SearchConditionDto cond = new SearchConditionDto(TEST_DATE);
			SearchConditionDto noDateCond = new SearchConditionDto(INVALID_DATE);
			Pageable pageable = Pageable.ofSize(24).withPage(0);
			Pageable halfPage = Pageable.ofSize(12);

			// when
			Page<HourlyResponseDto.HourlyChurnUser> chrunUserResult = service.getHourlyChurnUser(cond, pageable);
			Page<HourlyResponseDto.HourlyChurnUser> invalidDateResult = service.getHourlyChurnUser(noDateCond,
				pageable);
			Page<HourlyResponseDto.HourlyChurnUser> halfResult = service.getHourlyChurnUser(cond, halfPage);

			// then
			assertThat(chrunUserResult.getTotalElements()).isEqualTo(24);
			assertThat(chrunUserResult.stream()
				.mapToLong(HourlyResponseDto.HourlyChurnUser::getChurnUser)
				.reduce(Math::addExact)
				.orElseThrow())
				.isEqualTo(churnUserNum * 24);
			assertThat(invalidDateResult.getTotalElements()).isEqualTo(0);
			assertThat(chrunUserResult.getTotalPages()).isEqualTo(1);
			assertThat(halfResult.getPageable().next().getPageSize()).isEqualTo(12);
			assertThat(halfResult.getTotalPages()).isEqualTo(2);
		}

		@DisplayName("매출 조회")
		@Test
		void searchPayAmountByDateWithPage() {

			// given
			SearchConditionDto cond = new SearchConditionDto(TEST_DATE);
			SearchConditionDto noDateCond = new SearchConditionDto(INVALID_DATE);
			Pageable pageable = Pageable.ofSize(24).withPage(0);
			Pageable halfPage = Pageable.ofSize(12);

			// when
			Page<HourlyResponseDto.HourlyPayAmount> payAmountResult = service.getHourlyPayAmount(cond, pageable);
			Page<HourlyResponseDto.HourlyPayAmount> invalidDateResult = service.getHourlyPayAmount(noDateCond,
				pageable);
			Page<HourlyResponseDto.HourlyPayAmount> halfResult = service.getHourlyPayAmount(cond, halfPage);

			// then
			assertThat(payAmountResult.getTotalElements()).isEqualTo(24);
			assertThat(payAmountResult.stream().map(HourlyResponseDto.HourlyPayAmount::getPayAmount)
				.reduce(BigDecimal::add).orElseThrow())
				.isEqualByComparingTo(payAmountNum.multiply(BigDecimal.valueOf(24)));
			assertThat(invalidDateResult.getTotalElements()).isEqualTo(0);
			assertThat(payAmountResult.getTotalPages()).isEqualTo(1);
			assertThat(halfResult.getPageable().next().getPageSize()).isEqualTo(12);
			assertThat(halfResult.getTotalPages()).isEqualTo(2);
		}

		@DisplayName("비용 조회")
		@Test
		void searchCostByDateWithPage() {

			// given
			SearchConditionDto cond = new SearchConditionDto(TEST_DATE);
			SearchConditionDto noDateCond = new SearchConditionDto(INVALID_DATE);
			Pageable pageable = Pageable.ofSize(24).withPage(0);
			Pageable halfPage = Pageable.ofSize(12);

			// when
			Page<HourlyResponseDto.HourlyCost> costResult = repository.findHourlyCostWithConditions(cond, pageable);
			Page<HourlyResponseDto.HourlyCost> invalidDateResult = repository.findHourlyCostWithConditions(noDateCond,
				pageable);
			Page<HourlyResponseDto.HourlyCost> halfResult = repository.findHourlyCostWithConditions(cond, halfPage);

			// then
			assertThat(costResult.getTotalElements()).isEqualTo(24);
			assertThat(costResult.stream().map(HourlyResponseDto.HourlyCost::getCost)
				.reduce(BigDecimal::add).orElseThrow())
				.isEqualByComparingTo(costNum.multiply(BigDecimal.valueOf(24)));
			assertThat(invalidDateResult.getTotalElements()).isEqualTo(0);
			assertThat(costResult.getTotalPages()).isEqualTo(1);
			assertThat(halfResult.getPageable().next().getPageSize()).isEqualTo(12);
			assertThat(halfResult.getTotalPages()).isEqualTo(2);
		}

		@DisplayName("판매금액 조회")
		@Test
		void searchSalesAmountByDateWithPage() {

			// given
			SearchConditionDto cond = new SearchConditionDto(TEST_DATE);
			SearchConditionDto noDateCond = new SearchConditionDto(INVALID_DATE);
			Pageable pageable = Pageable.ofSize(24).withPage(0);
			Pageable halfPage = Pageable.ofSize(12);

			// when
			Page<HourlyResponseDto.HourlySalesAmount> result = service.getHourlySalesAmount(cond, pageable);
			Page<HourlyResponseDto.HourlySalesAmount> invalidDateResult = service.getHourlySalesAmount(noDateCond,
				pageable);
			Page<HourlyResponseDto.HourlySalesAmount> halfResult = service.getHourlySalesAmount(cond, halfPage);

			// then
			assertThat(result.getTotalElements()).isEqualTo(24);
			assertThat(result.stream().map(HourlyResponseDto.HourlySalesAmount::getSalesAmount)
				.reduce(BigDecimal::add).orElseThrow())
				.isEqualByComparingTo(salesAmountNum.multiply(BigDecimal.valueOf(24)));
			assertThat(invalidDateResult.getTotalElements()).isEqualTo(0);
			assertThat(result.getTotalPages()).isEqualTo(1);
			assertThat(halfResult.getPageable().next().getPageSize()).isEqualTo(12);
			assertThat(halfResult.getTotalPages()).isEqualTo(2);
		}
	}

	@Nested
	@DisplayName("시간별 데이터 날짜범위 조회")
	class SearchHourlyDataByDateRange {

		@DisplayName("신규유저 조회")
		@Test
		void searchNewUserByDateRange() {

			// given
			SearchConditionDto dateRangeCond = new SearchConditionDto(DATE_START, DATE_END);
			int rangeDayCount = DATE_END.getDayOfMonth() - DATE_START.getDayOfMonth() + 1;
			Pageable pageable = Pageable.ofSize(24).withPage(0);

			// when
			Page<HourlyResponseDto.HourlyNewUser> dateRangeResult = service.getHourlyNewUser(dateRangeCond, pageable);

			// then
			assertThat(dateRangeResult.getTotalElements()).isEqualTo(rangeDayCount);
		}

		@DisplayName("이탈유저 조회")
		@Test
		void searchChurnUserByDateRange() {

			// given
			SearchConditionDto dateRangeCond = new SearchConditionDto(DATE_START, DATE_END);
			int rangeDayCount = DATE_END.getDayOfMonth() - DATE_START.getDayOfMonth() + 1;
			Pageable pageable = Pageable.ofSize(24).withPage(0);

			// when
			Page<HourlyResponseDto.HourlyChurnUser> dateRangeResult = service.getHourlyChurnUser(dateRangeCond,
				pageable);

			// then
			assertThat(dateRangeResult.getTotalElements()).isEqualTo(rangeDayCount);
		}

		@DisplayName("매출 조회")
		@Test
		void searchPayAmountByDateRange() {

			// given
			SearchConditionDto dateRangeCond = new SearchConditionDto(DATE_START, DATE_END);
			int rangeDayCount = DATE_END.getDayOfMonth() - DATE_START.getDayOfMonth() + 1;
			Pageable pageable = Pageable.ofSize(24).withPage(0);

			// when
			Page<HourlyResponseDto.HourlyPayAmount> dateRangeResult = service.getHourlyPayAmount(dateRangeCond,
				pageable);

			// then
			assertThat(dateRangeResult.getTotalElements()).isEqualTo(rangeDayCount);
		}

		@DisplayName("비용 조회")
		@Test
		void searchCostByDateRange() {

			// given
			SearchConditionDto dateRangeCond = new SearchConditionDto(DATE_START, DATE_END);
			int rangeDayCount = DATE_END.getDayOfMonth() - DATE_START.getDayOfMonth() + 1;
			Pageable pageable = Pageable.ofSize(24).withPage(0);

			// when
			Page<HourlyResponseDto.HourlyCost> dateRangeResult = repository.findHourlyCostWithConditions(dateRangeCond,
				pageable);
			// then
			assertThat(dateRangeResult.getTotalElements()).isEqualTo(rangeDayCount);
		}

		@DisplayName("판매금액 조회")
		@Test
		void searchSalesAmountByDateRange() {

			// given
			SearchConditionDto dateRangeCond = new SearchConditionDto(DATE_START, DATE_END);
			int rangeDayCount = DATE_END.getDayOfMonth() - DATE_START.getDayOfMonth() + 1;
			Pageable pageable = Pageable.ofSize(24).withPage(0);

			// when
			Page<HourlyResponseDto.HourlySalesAmount> dateRangeResult = service.getHourlySalesAmount(dateRangeCond,
				pageable);

			// then
			assertThat(dateRangeResult.getTotalElements()).isEqualTo(rangeDayCount);
		}
	}
}