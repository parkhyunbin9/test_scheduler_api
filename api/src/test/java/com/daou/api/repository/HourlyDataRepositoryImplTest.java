package com.daou.api.repository;

import static com.daou.api.model.QHourlyData.*;
import static org.assertj.core.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.AfterEach;
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
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

@SpringBootTest
@Transactional
class HourlyDataRepositoryImplTest {

	final int TEST_HOUR = 3;
	final int WRONG_TEST_HOUR = 23;
	final LocalDate START_DATE = LocalDate.of(2022, 12, 5);
	final LocalDate END_DATE = LocalDate.of(2022, 12, 15);
	final LocalDate TEST_DATE = LocalDate.of(2022, 12, 1);
	final LocalDate WRONG_TEST_DATE = LocalDate.of(2022, 11, 1);
	@Autowired
	HourlyDataRepository repository;
	@Autowired
	EntityManager em;
	JPAQueryFactory queryFactory;
	List<HourlyData> testData;
	List<HourlyData> rangeData;

	@BeforeEach
	public void init() {
		testData = IntStream.range(0, 23).mapToObj(i ->
			HourlyData.builder()
				.date(TEST_DATE)
				.hour(i)
				.newUser(1L)
				.churnUser(3L)
				.payAmount(BigDecimal.valueOf(100L))
				.cost(BigDecimal.valueOf(100L))
				.salesAmount(BigDecimal.valueOf(100L))
				.build()
		).collect(Collectors.toList());

		repository.saveAll(testData);
		rangeData = IntStream.range(5, 15).mapToObj(i ->
			HourlyData.builder()
				.date(LocalDate.of(2022, 12, i))
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

	@AfterEach
	public void tearDown() {
		repository.deleteAll();
	}

	@DisplayName("페이징 테스트")
	@Test
	void searchWithPaging() {

		// given
		SearchConditionDto searchConditionDto = new SearchConditionDto(TEST_DATE);
		Pageable pageable = Pageable.ofSize(2).withPage(0);
		HourlyDataRepositoryImpl hourlyDataRepository = new HourlyDataRepositoryImpl(em);

		// when
		Page<HourlyResponseDto.HourlyNewUser> pagingResult = hourlyDataRepository.findHourlyNewUserWithConditions(
			searchConditionDto, pageable);

		// then
		assertThat(pagingResult.getTotalElements()).isEqualTo(23);
		assertThat(pagingResult.getSize()).isEqualTo(2);
		assertThat(pagingResult.getTotalPages()).isEqualTo(24 / 2);
	}

	private void buildHourlyData(int from, int to) {
		List<HourlyData> buildData = IntStream.range(from, to + 1).mapToObj(i ->
			HourlyData.builder()
				.date(TEST_DATE)
				.hour(i)
				.newUser(1L)
				.churnUser(3L)
				.payAmount(BigDecimal.valueOf(100L))
				.cost(BigDecimal.valueOf(100L))
				.salesAmount(BigDecimal.valueOf(100L))
				.build()
		).collect(Collectors.toList());
		repository.saveAll(buildData);
	}

	@Nested
	@DisplayName("다양한 조건에 따른 QueryDsl을 이용한 조회")
	class SearchByCondition {

		@DisplayName("시간 Equal")
		@Test
		void searchByHourEqualCondition() throws NoSuchMethodException {

			// given
			queryFactory = new JPAQueryFactory(em);
			SearchConditionDto hourCondition = new SearchConditionDto(TEST_HOUR);
			SearchConditionDto wrong_Condition = new SearchConditionDto(WRONG_TEST_HOUR);
			HourlyDataRepositoryImpl hourlyDataRepository = new HourlyDataRepositoryImpl(em);
			Method hourEq = hourlyDataRepository.getClass().getDeclaredMethod("hourEq", SearchConditionDto.class);
			hourEq.setAccessible(true);

			try {
				//when
				List<HourlyData> searchResult = queryFactory.selectFrom(hourlyData)
					.where((BooleanExpression)hourEq.invoke(hourlyDataRepository, hourCondition))
					.fetch();

				List<HourlyData> findZeroResult = queryFactory.selectFrom(hourlyData)
					.where((BooleanExpression)hourEq.invoke(hourlyDataRepository, wrong_Condition))
					.fetch();

				//then
				assertThat(searchResult.size()).isEqualTo(1);
				assertThat(findZeroResult.size()).isEqualTo(0);
				assertThat(searchResult.get(0).getHour()).isEqualTo(TEST_HOUR);

			} catch (IllegalAccessException | InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		}

		@DisplayName("시간 GreaterOrEqual ")
		@Test
		void searchByHourGOE() throws NoSuchMethodException {

			// given
			repository.deleteAll();
			queryFactory = new JPAQueryFactory(em);
			buildHourlyData(0, 23);
			final int START_TIME = 22;
			int timeDelta = 23 - START_TIME + 1;
			SearchConditionDto hourGoeCondition = new SearchConditionDto();
			hourGoeCondition.setStartHour(START_TIME);
			HourlyDataRepositoryImpl hourlyDataRepository = new HourlyDataRepositoryImpl(em);
			Method hourGoe = hourlyDataRepository.getClass().getDeclaredMethod("hourGoe", SearchConditionDto.class);
			hourGoe.setAccessible(true);

			try {

				//when
				List<HourlyData> searchResult = queryFactory.selectFrom(hourlyData)
					.where((BooleanExpression)hourGoe.invoke(hourlyDataRepository, hourGoeCondition))
					.fetch();

				//then
				assertThat(searchResult.size()).isEqualTo(timeDelta);
				assertThat(searchResult.get(0).getHour()).isBetween(START_TIME, 23);

			} catch (IllegalAccessException | InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		}

		@DisplayName("시간 LessOrEqual")
		@Test
		void searchByHourLOE() throws NoSuchMethodException {

			// given
			repository.deleteAll();
			queryFactory = new JPAQueryFactory(em);
			buildHourlyData(0, 23);
			final int END_TIME = 5;
			int timeDelta = END_TIME + 1;
			SearchConditionDto hourLoeCondition = new SearchConditionDto();
			hourLoeCondition.setEndHour(END_TIME);
			HourlyDataRepositoryImpl hourlyDataRepository = new HourlyDataRepositoryImpl(em);
			Method hourLoe = hourlyDataRepository.getClass().getDeclaredMethod("hourLoe", SearchConditionDto.class);
			hourLoe.setAccessible(true);

			try {

				//when
				List<HourlyData> searchResult = queryFactory.selectFrom(hourlyData)
					.where((BooleanExpression)hourLoe.invoke(hourlyDataRepository, hourLoeCondition))
					.fetch();

				//then
				assertThat(searchResult.size()).isEqualTo(timeDelta);
				assertThat(searchResult.get(0).getHour()).isBetween(0, END_TIME);
			} catch (IllegalAccessException | InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		}

		@DisplayName("시간 Range")
		@Test
		void searchByHourRange() throws NoSuchMethodException {

			// given
			queryFactory = new JPAQueryFactory(em);
			final int START_TIME = 0;
			final int END_TIME = 10;
			int timeDelta = END_TIME - START_TIME + 1;
			SearchConditionDto hourRangeCondition = new SearchConditionDto(START_TIME, END_TIME);
			HourlyDataRepositoryImpl hourlyDataRepository = new HourlyDataRepositoryImpl(em);

			Method hourGoe = hourlyDataRepository.getClass().getDeclaredMethod("hourGoe", SearchConditionDto.class);
			hourGoe.setAccessible(true);
			Method hourLoe = hourlyDataRepository.getClass().getDeclaredMethod("hourLoe", SearchConditionDto.class);
			hourLoe.setAccessible(true);

			try {
				//when
				List<HourlyData> searchResult = queryFactory.selectFrom(hourlyData)
					.where(
						(BooleanExpression)hourGoe.invoke(hourlyDataRepository, hourRangeCondition),
						(BooleanExpression)hourLoe.invoke(hourlyDataRepository, hourRangeCondition)
					)
					.fetch();

				//then
				assertThat(searchResult.size()).isEqualTo(timeDelta);
				assertThat(searchResult.get(0).getHour()).isBetween(START_TIME, END_TIME);
			} catch (IllegalAccessException | InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		}

		@DisplayName("날짜 Equal")
		@Test
		void searchByDateEq() throws NoSuchMethodException {

			// given
			queryFactory = new JPAQueryFactory(em);
			SearchConditionDto dateCondition = new SearchConditionDto(TEST_DATE);
			SearchConditionDto wrongCondition = new SearchConditionDto(WRONG_TEST_DATE);

			HourlyDataRepositoryImpl hourlyDataRepository = new HourlyDataRepositoryImpl(em);
			Method dateEq = hourlyDataRepository.getClass().getDeclaredMethod("dateEq", SearchConditionDto.class);
			dateEq.setAccessible(true);

			try {

				//when
				List<HourlyData> searchResult = queryFactory.selectFrom(hourlyData)
					.where((BooleanExpression)dateEq.invoke(hourlyDataRepository, dateCondition))
					.fetch();
				List<HourlyData> findZeroResult = queryFactory.selectFrom(hourlyData)
					.where((BooleanExpression)dateEq.invoke(hourlyDataRepository, wrongCondition))
					.fetch();

				//then
				assertThat(findZeroResult.size()).isEqualTo(0);
				assertThat(searchResult.size()).isEqualTo(testData.size());
				assertThat(searchResult.get(0).getDate()).isEqualTo(TEST_DATE);

			} catch (IllegalAccessException | InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		}

		@DisplayName("날짜 GreaterOrEqual")
		@Test
		void searchByDateGOE() throws NoSuchMethodException {

			// given
			queryFactory = new JPAQueryFactory(em);
			int goeCount = 3;
			SearchConditionDto dateGoeCondition = new SearchConditionDto();
			dateGoeCondition.setStartDate(END_DATE.minusDays(goeCount));
			HourlyDataRepositoryImpl hourlyDataRepository = new HourlyDataRepositoryImpl(em);
			Method dateGoe = hourlyDataRepository.getClass().getDeclaredMethod("dateGoe", SearchConditionDto.class);
			dateGoe.setAccessible(true);

			try {

				//when
				List<HourlyData> searchResult = queryFactory.selectFrom(hourlyData)
					.where((BooleanExpression)dateGoe.invoke(hourlyDataRepository, dateGoeCondition))
					.fetch();

				//then
				assertThat(searchResult.size()).isEqualTo(goeCount);
				assertThat(searchResult.get(0).getDate()).isAfterOrEqualTo(END_DATE.minusDays(goeCount));

			} catch (IllegalAccessException | InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		}

		@DisplayName("날짜 LessOrEqual")
		@Test
		void searchByDateLOE() throws NoSuchMethodException {

			// given
			queryFactory = new JPAQueryFactory(em);
			int loeDays = rangeData.size() + testData.size();
			SearchConditionDto dateLoeCondition = new SearchConditionDto();
			dateLoeCondition.setEndDate(END_DATE);

			HourlyDataRepositoryImpl hourlyDataRepository = new HourlyDataRepositoryImpl(em);
			Method dateLoe = hourlyDataRepository.getClass().getDeclaredMethod("dateLoe", SearchConditionDto.class);
			dateLoe.setAccessible(true);

			try {

				//when
				List<HourlyData> searchResult = queryFactory.selectFrom(hourlyData)
					.where((BooleanExpression)dateLoe.invoke(hourlyDataRepository, dateLoeCondition))
					.fetch();

				//then
				assertThat(searchResult.size()).isEqualTo(loeDays);
				assertThat(searchResult.get(0).getDate()).isBeforeOrEqualTo(END_DATE);

			} catch (InvocationTargetException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

		@DisplayName("날짜 Range")
		@Test
		void searchByDateRange() throws NoSuchMethodException {

			// given
			queryFactory = new JPAQueryFactory(em);
			int dayOfStart = START_DATE.getDayOfMonth();
			int dayOfEnd = END_DATE.getDayOfMonth();
			int dateDelta = dayOfEnd - dayOfStart;
			SearchConditionDto dateRangeCondition = new SearchConditionDto(START_DATE, END_DATE);
			HourlyDataRepositoryImpl hourlyDataRepository = new HourlyDataRepositoryImpl(em);
			Method dateGoe = hourlyDataRepository.getClass().getDeclaredMethod("dateGoe", SearchConditionDto.class);
			dateGoe.setAccessible(true);
			Method dateLoe = hourlyDataRepository.getClass().getDeclaredMethod("dateLoe", SearchConditionDto.class);
			dateLoe.setAccessible(true);

			try {

				// when
				List<HourlyData> searchResult = queryFactory.selectFrom(hourlyData)
					.where(
						(BooleanExpression)dateGoe.invoke(hourlyDataRepository, dateRangeCondition),
						(BooleanExpression)dateLoe.invoke(hourlyDataRepository, dateRangeCondition)
					)
					.fetch();

				// then
				assertThat(searchResult.size()).isEqualTo(dateDelta);
				assertThat(searchResult.get(0).getDate()).isBetween(START_DATE, END_DATE);
			} catch (InvocationTargetException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
	}

}