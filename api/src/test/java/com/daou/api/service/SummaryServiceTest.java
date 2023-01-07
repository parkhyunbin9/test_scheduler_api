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
import org.springframework.transaction.annotation.Transactional;

import com.daou.api.dto.request.search.SearchConditionDto;
import com.daou.api.dto.response.SummaryResponseDto;
import com.daou.api.model.DailySummary;
import com.daou.api.model.HourlySummary;
import com.daou.api.repository.DailySummaryRepository;
import com.daou.api.repository.HourlySummaryRepository;

@SpringBootTest
@Transactional
class SummaryServiceTest {

	final LocalDate TEST_DATE = LocalDate.of(2022, 12, 1);
	final LocalDate INVALID_DATE = LocalDate.of(2022, 11, 1);
	@Autowired
	SummaryService service;
	@Autowired
	HourlySummaryRepository hourlyRepository;
	@Autowired
	DailySummaryRepository dailyRepository;
	List<HourlySummary> hourlyTestData;
	List<DailySummary> dailyTestData;

	@BeforeEach
	public void init() {

		hourlyTestData = IntStream.range(0, 23).mapToObj(i ->
			HourlySummary.builder()
				.hour(i)
				.sumNewUser(100L)
				.sumChurnUser(100L)
				.sumPayAmount(BigDecimal.valueOf(100L))
				.sumCost(BigDecimal.valueOf(100L))
				.sumSalesAmount(BigDecimal.valueOf(100L))
				.avgNewUser(Double.valueOf("10"))
				.avgChurnUser(Double.valueOf("10"))
				.avgPayAmount(Double.valueOf("10"))
				.avgCost(Double.valueOf("10"))
				.avgSalesAmount(Double.valueOf("10"))
				.maxNewUser(100L)
				.maxChurnUser(100L)
				.maxPayAmount(BigDecimal.valueOf(100L))
				.maxCost(BigDecimal.valueOf(100L))
				.maxSalesAmount(BigDecimal.valueOf(100L))
				.minNewUser(100L)
				.minChurnUser(100L)
				.minPayAmount(BigDecimal.valueOf(100L))
				.minCost(BigDecimal.valueOf(100L))
				.minSalesAmount(BigDecimal.valueOf(100L))
				.build()
		).collect(Collectors.toList());
		hourlyRepository.saveAll(hourlyTestData);

		dailyTestData = IntStream.range(1, 31).mapToObj(i ->
			DailySummary.builder()
				.date(LocalDate.of(2022, 12, i))
				.sumNewUser(100L)
				.sumChurnUser(100L)
				.sumPayAmount(BigDecimal.valueOf(100L))
				.sumCost(BigDecimal.valueOf(100L))
				.sumSalesAmount(BigDecimal.valueOf(100L))
				.avgNewUser(Double.valueOf("10"))
				.avgChurnUser(Double.valueOf("10"))
				.avgPayAmount(Double.valueOf("10"))
				.avgCost(Double.valueOf("10"))
				.avgSalesAmount(Double.valueOf("10"))
				.maxNewUser(100L)
				.maxChurnUser(100L)
				.maxPayAmount(BigDecimal.valueOf(100L))
				.maxCost(BigDecimal.valueOf(100L))
				.maxSalesAmount(BigDecimal.valueOf(100L))
				.minNewUser(100L)
				.minChurnUser(100L)
				.minPayAmount(BigDecimal.valueOf(100L))
				.minCost(BigDecimal.valueOf(100L))
				.minSalesAmount(BigDecimal.valueOf(100L))
				.build()
		).collect(Collectors.toList());

		dailyRepository.saveAll(dailyTestData);
	}

	@Nested
	@DisplayName("시간별 요약 데이터 조회")
	class SearchHourlySummaryByConditions {

		@DisplayName("시간 Equal")
		@Test
		void searchHourlySummaryByHour() {

			// given
			SearchConditionDto cond = new SearchConditionDto(5);
			SearchConditionDto noHourCond = new SearchConditionDto(23);

			// when
			List<SummaryResponseDto.Hourly> result = service.getHourlySummary(cond);
			List<SummaryResponseDto.Hourly> noResult = service.getHourlySummary(noHourCond);

			// then
			assertThat(result.size()).isEqualTo(1);
			assertThat(result.get(0).getHour()).isEqualTo(5);
			assertThat(noResult.size()).isEqualTo(0);
		}

		@DisplayName("시간 LessOrEqual")
		@Test
		void searchHourlySummaryByHourLOE() {

			// given
			SearchConditionDto hourLoeCond = new SearchConditionDto();
			hourLoeCond.setEndHour(5);

			// when
			List<SummaryResponseDto.Hourly> result = service.getHourlySummary(hourLoeCond);

			// then
			assertThat(result.size()).isEqualTo(6);
			assertThat(result).extracting("hour").contains(0);
			assertThat(result).extracting("hour").contains(5);

		}

		@DisplayName("시간 GreaterOrEqual")
		@Test
		void searchHourlySummaryByHourGOE() {

			// given
			SearchConditionDto hourGoeCond = new SearchConditionDto();
			hourGoeCond.setStartHour(20);
			int hourDelta = hourlyTestData.size() - 20;
			// when
			List<SummaryResponseDto.Hourly> result = service.getHourlySummary(hourGoeCond);

			// then
			assertThat(result.size()).isEqualTo(hourDelta);
			assertThat(result).extracting("hour").contains(hourlyTestData.size() - 1);
			assertThat(result).extracting("hour").contains(20);

		}

		@DisplayName("시간 Range")
		@Test
		void searchHourlySummaryByHourRange() {

			// given
			SearchConditionDto hourRangeCond = new SearchConditionDto();
			hourRangeCond.setStartHour(5);
			hourRangeCond.setEndHour(10);

			// when
			List<SummaryResponseDto.Hourly> result = service.getHourlySummary(hourRangeCond);

			// then
			assertThat(result.size()).isEqualTo(6);
			assertThat(result).extracting("hour").contains(5);
			assertThat(result).extracting("hour").contains(10);

		}
	}

	@DisplayName("일별 요약 데이터 조회")
	@Nested
	class SearchDailySummaryByConditions {

		@DisplayName("날짜 Equal")
		@Test
		void searchDailySummaryByDate() {

			// given
			SearchConditionDto cond = new SearchConditionDto(TEST_DATE);
			SearchConditionDto noDateCond = new SearchConditionDto(INVALID_DATE);

			// when
			List<SummaryResponseDto.Daily> result = service.getDailySummary(cond);
			List<SummaryResponseDto.Daily> noResult = service.getDailySummary(noDateCond);

			// then
			assertThat(result.size()).isEqualTo(1);
			assertThat(result.get(0).getDate()).isEqualTo(TEST_DATE);
			assertThat(noResult.size()).isEqualTo(0);
		}

		@DisplayName("날짜 LessOrEqual")
		@Test
		void searchByDailySummaryByDateLOE() {

			// given
			int plusDay = 3;
			SearchConditionDto dateLoeCond = new SearchConditionDto();
			dateLoeCond.setEndDate(TEST_DATE.plusDays(plusDay));
			int dateDelta = plusDay + 1;

			// when
			List<SummaryResponseDto.Daily> result = service.getDailySummary(dateLoeCond);

			// then
			assertThat(result.size()).isEqualTo(dateDelta);
			assertThat(result).extracting("date").contains(TEST_DATE.plusDays(plusDay));
		}

		@DisplayName("날짜 GreaterOrEqual")
		@Test
		void searchByDailySummaryByDateGOE() {

			// given
			int plusDay = 3;
			SearchConditionDto dateGoeCond = new SearchConditionDto();
			dateGoeCond.setStartDate(TEST_DATE.plusDays(plusDay));
			int dateDelta = dailyTestData.size() - plusDay;

			// when
			List<SummaryResponseDto.Daily> result = service.getDailySummary(dateGoeCond);

			// then
			assertThat(result.size()).isEqualTo(dateDelta);
			assertThat(result).extracting("date").contains(TEST_DATE.plusDays(plusDay));
		}

		@DisplayName("날짜 Range")
		@Test
		void searchByDailySummaryByDateRange() {

			// given
			int plusDay = 3;
			SearchConditionDto dateRangeCond = new SearchConditionDto();
			dateRangeCond.setStartDate(TEST_DATE);
			dateRangeCond.setEndDate(TEST_DATE.plusDays(plusDay));
			int dateDelta = plusDay + 1;

			// when
			List<SummaryResponseDto.Daily> result = service.getDailySummary(dateRangeCond);

			// then
			assertThat(result.size()).isEqualTo(dateDelta);
			assertThat(result).extracting("date").contains(TEST_DATE);
			assertThat(result).extracting("date").contains(TEST_DATE.plusDays(plusDay));
		}
	}
}