package com.daou.api.repository;

import static com.daou.api.model.QDailySummary.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.util.BigDecimalComparator.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.daou.api.dto.request.DailySummaryRequestDto;
import com.daou.api.dto.request.search.SearchConditionDto;
import com.daou.api.model.DailySummary;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

@SpringBootTest
@Transactional
class DailySummaryRepositoryImplTest {
	final LocalDate TEST_DATE = LocalDate.of(2022, 12, 1);
	final LocalDate WRONG_DATE = LocalDate.of(2022, 11, 1);
	final LocalDate START_DATE = LocalDate.of(2022, 12, 5);
	final LocalDate END_DATE = LocalDate.of(2022, 12, 15);

	@Autowired
	DailySummaryRepository repository;
	@Autowired
	EntityManager em;
	JPAQueryFactory queryFactory;
	List<DailySummary> testData;

	@BeforeEach
	public void init() {

		testData = IntStream.range(1, 31).mapToObj(i ->
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

		repository.saveAll(testData);
	}

	@Test
	void DailySummary_DATE_EQUAL_조회() throws NoSuchMethodException {

		// given
		queryFactory = new JPAQueryFactory(em);
		SearchConditionDto dateCondition = new SearchConditionDto(TEST_DATE);
		SearchConditionDto wrongCondition = new SearchConditionDto(WRONG_DATE);

		DailySummaryRepositoryImpl dailySummaryRepository = new DailySummaryRepositoryImpl(em);
		Method dateEq = dailySummaryRepository.getClass().getDeclaredMethod("dateEq", SearchConditionDto.class);
		dateEq.setAccessible(true);

		try {

			//when
			List<DailySummary> searchResult = queryFactory.selectFrom(dailySummary)
				.where((BooleanExpression)dateEq.invoke(dailySummaryRepository, dateCondition))
				.fetch();
			List<DailySummary> findZeroResult = queryFactory.selectFrom(dailySummary)
				.where((BooleanExpression)dateEq.invoke(dailySummaryRepository, wrongCondition))
				.fetch();

			//then
			assertThat(findZeroResult.size()).isEqualTo(0);
			assertThat(searchResult.size()).isEqualTo(1);
			assertThat(searchResult.get(0).getDate()).isEqualTo(TEST_DATE);

		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	void DailySummary_DATE_Goe_조회() throws NoSuchMethodException {

		// given
		queryFactory = new JPAQueryFactory(em);
		int goeCount = testData.size() - END_DATE.getDayOfMonth() + 1;
		SearchConditionDto dateGoeCondition = new SearchConditionDto();
		dateGoeCondition.setStartDate(END_DATE);
		DailySummaryRepositoryImpl dailySummaryRepository = new DailySummaryRepositoryImpl(em);
		Method dateGoe = dailySummaryRepository.getClass().getDeclaredMethod("dateGoe", SearchConditionDto.class);
		dateGoe.setAccessible(true);

		try {

			//when
			List<DailySummary> searchResult = queryFactory.selectFrom(dailySummary)
				.where((BooleanExpression)dateGoe.invoke(dailySummaryRepository, dateGoeCondition))
				.fetch();

			//then
			assertThat(searchResult.size()).isEqualTo(goeCount);
			assertThat(searchResult.get(0).getDate()).isAfterOrEqualTo(END_DATE.minusDays(goeCount));

		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	void DailySummary_DATE_Loe_조회() throws NoSuchMethodException {

		// given
		queryFactory = new JPAQueryFactory(em);
		int loeDays = START_DATE.getDayOfMonth();
		SearchConditionDto dateLoeCondition = new SearchConditionDto();
		dateLoeCondition.setEndDate(START_DATE);

		DailySummaryRepositoryImpl dailySummaryRepository = new DailySummaryRepositoryImpl(em);
		Method dateLoe = dailySummaryRepository.getClass().getDeclaredMethod("dateLoe", SearchConditionDto.class);
		dateLoe.setAccessible(true);

		try {

			//when
			List<DailySummary> searchResult = queryFactory.selectFrom(dailySummary)
				.where((BooleanExpression)dateLoe.invoke(dailySummaryRepository, dateLoeCondition))
				.fetch();

			//then
			assertThat(searchResult.size()).isEqualTo(loeDays);
			assertThat(searchResult.get(0).getDate()).isBeforeOrEqualTo(START_DATE);

		} catch (InvocationTargetException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	void DailySummary_DATE_RANGE_조회() throws NoSuchMethodException {

		// given
		queryFactory = new JPAQueryFactory(em);
		int dayOfStart = START_DATE.getDayOfMonth();
		int dayOfEnd = END_DATE.getDayOfMonth();
		int dateDelta = dayOfEnd - dayOfStart + 1;
		SearchConditionDto dateRangeCondition = new SearchConditionDto(START_DATE, END_DATE);
		DailySummaryRepositoryImpl dailySummaryRepository = new DailySummaryRepositoryImpl(em);
		Method dateGoe = dailySummaryRepository.getClass().getDeclaredMethod("dateGoe", SearchConditionDto.class);
		dateGoe.setAccessible(true);
		Method dateLoe = dailySummaryRepository.getClass().getDeclaredMethod("dateLoe", SearchConditionDto.class);
		dateLoe.setAccessible(true);

		try {

			// when
			List<DailySummary> searchResult = queryFactory.selectFrom(dailySummary)
				.where(
					(BooleanExpression)dateGoe.invoke(dailySummaryRepository, dateRangeCondition),
					(BooleanExpression)dateLoe.invoke(dailySummaryRepository, dateRangeCondition)
				)
				.fetch();

			// then
			assertThat(searchResult.size()).isEqualTo(dateDelta);
			assertThat(searchResult.get(0).getDate()).isBetween(START_DATE, END_DATE);
		} catch (InvocationTargetException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	void 저장_dailySumamry() {
		// given
		DailySummaryRequestDto.SaveRequest saveRequest = new DailySummaryRequestDto.SaveRequest(
			LocalDate.of(2022, 01, 01),
			0L, 0L, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
			0D, 0D, 0D, 0D, 0D,
			0L, 0L, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
			0L, 0L, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
		DailySummary newDailySummary = saveRequest.toEntity();

		// when
		DailySummary savedData = repository.save(newDailySummary);
		repository.flush();

		// then
		assertThat(savedData).isSameAs(newDailySummary);
		assertThat(savedData)
			.usingComparatorForType(BIG_DECIMAL_COMPARATOR, BigDecimal.class)
			.usingRecursiveComparison()
			.isEqualTo(newDailySummary);

	}

	@Test
	void 저장된_데이터_조회() {
		// given
		DailySummaryRequestDto.SaveRequest saveRequest = new DailySummaryRequestDto.SaveRequest(
			LocalDate.of(2022, 01, 01),
			1L, 1L, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
			1D, 1D, 1D, 1D, 1D,
			1L, 1L, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
			1L, 1L, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
		DailySummary newDailySummary = saveRequest.toEntity();
		DailySummaryRequestDto.SaveRequest saveRequest2 = new DailySummaryRequestDto.SaveRequest(
			LocalDate.of(2022, 01, 02),
			10L, 10L, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE,
			10D, 10D, 10D, 10D, 10D,
			10L, 10L, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE,
			10L, 10L, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE);
		DailySummary newDailySummary2 = saveRequest2.toEntity();
		DailySummary savedData = repository.save(newDailySummary);
		DailySummary savedData2 = repository.save(newDailySummary2);
		repository.flush();
		em.clear();

		// when
		DailySummary findData = repository.findByDate(LocalDate.of(2022, 01, 01))
			.orElseThrow(() -> new IllegalArgumentException("해당 날짜의 집계데이터가 없습니다."));
		DailySummary findData2 = repository.findByDate(LocalDate.of(2022, 01, 02).minusDays(1))
			.orElseThrow(() -> new IllegalArgumentException("해당 날짜의 집계데이터가 없습니다."));

		// then
		assertThat(savedData)
			.usingComparatorForType(BIG_DECIMAL_COMPARATOR, BigDecimal.class)
			.usingRecursiveComparison()
			.isEqualTo(newDailySummary);

		assertThat(savedData2)
			.usingComparatorForType(BIG_DECIMAL_COMPARATOR, BigDecimal.class)
			.usingRecursiveComparison()
			.isEqualTo(newDailySummary2);
	}

	@Test
	void 수정_dailySummary() {
		// given
		repository.deleteAll();
		DailySummaryRequestDto.SaveRequest saveRequest = new DailySummaryRequestDto.SaveRequest(
			LocalDate.of(2022, 01, 01),
			0L, 0L, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
			0D, 0D, 0D, 0D, 0D,
			0L, 0L, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
			0L, 0L, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
		DailySummary newDailySummary = saveRequest.toEntity();
		DailySummary savedData = repository.save(newDailySummary);
		DailySummary beUpdateData = new DailySummaryRequestDto.SaveRequest(LocalDate.of(2022, 01, 02),
			1L, 1L, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN,
			1D, 1D, 1D, 1D, 1D,
			1L, 1L, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN,
			1L, 1L, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN).toEntity();
		repository.flush();
		em.clear();

		// when
		DailySummary findData = repository.findByDate(LocalDate.of(2022, 01, 01))
			.orElseThrow(() -> new IllegalArgumentException("요청하신 날짜의 집계데이터가 없습니다."));
		findData.change(beUpdateData);
		repository.flush();

		DailySummary updateData = repository.findByDate(LocalDate.of(2022, 01, 01))
			.orElseThrow(() -> new IllegalArgumentException("요청하신 날짜의 집계데이터가 없습니다."));

		//then
		assertThat(savedData.getId()).isEqualTo(updateData.getId());
		assertThat(savedData.getDate()).isEqualTo(updateData.getDate());

		assertThat(savedData.getSumNewUser()).isNotEqualTo(updateData.getSumNewUser());
		assertThat(savedData.getSumChurnUser()).isNotEqualTo(updateData.getSumChurnUser());
		assertThat(savedData.getSumPayAmount()).isNotEqualByComparingTo(updateData.getSumPayAmount());
		assertThat(savedData.getSumCost()).isNotEqualByComparingTo(updateData.getSumCost());
		assertThat(savedData.getSumSalesAmount()).isNotEqualByComparingTo(updateData.getSumSalesAmount());

		assertThat(savedData.getAvgNewUser()).isNotEqualTo(updateData.getAvgNewUser());
		assertThat(savedData.getAvgChurnUser()).isNotEqualTo(updateData.getAvgChurnUser());
		assertThat(savedData.getAvgPayAmount()).isNotEqualTo(updateData.getAvgPayAmount());
		assertThat(savedData.getAvgCost()).isNotEqualTo(updateData.getAvgCost());
		assertThat(savedData.getAvgSalesAmount()).isNotEqualTo(updateData.getAvgSalesAmount());

		assertThat(savedData.getMaxNewUser()).isNotEqualTo(updateData.getMaxNewUser());
		assertThat(savedData.getMaxChurnUser()).isNotEqualTo(updateData.getMaxChurnUser());
		assertThat(savedData.getMaxPayAmount()).isNotEqualByComparingTo(updateData.getMaxPayAmount());
		assertThat(savedData.getMaxCost()).isNotEqualByComparingTo(updateData.getMaxCost());
		assertThat(savedData.getMaxSalesAmount()).isNotEqualByComparingTo(updateData.getMaxSalesAmount());

		assertThat(savedData.getMinNewUser()).isNotEqualTo(updateData.getMinNewUser());
		assertThat(savedData.getMinChurnUser()).isNotEqualTo(updateData.getMinChurnUser());
		assertThat(savedData.getMinPayAmount()).isNotEqualByComparingTo(updateData.getMinPayAmount());
		assertThat(savedData.getMinCost()).isNotEqualByComparingTo(updateData.getMinCost());
		assertThat(savedData.getMinSalesAmount()).isNotEqualByComparingTo(updateData.getMinSalesAmount());

		assertThatThrownBy(() -> repository.findByDate(LocalDate.of(2022, 01, 02))
			.orElseThrow(() -> new IllegalArgumentException("요청하신 날짜의 집계데이터가 없습니다.")))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void 삭제_dailySumamry() {

		// given
		repository.deleteAll();
		DailySummaryRequestDto.SaveRequest saveRequest = new DailySummaryRequestDto.SaveRequest(
			LocalDate.of(2022, 01, 01),
			0L, 0L, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
			0D, 0D, 0D, 0D, 0D,
			0L, 0L, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
			0L, 0L, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
		DailySummary newDailySummary = saveRequest.toEntity();

		// when
		DailySummary savedData = repository.save(newDailySummary);
		repository.flush();

		DailySummary findData = repository.findByDate(LocalDate.of(2022, 01, 01))
			.orElseThrow(() -> new IllegalArgumentException());
		repository.delete(findData);
		repository.flush();
		em.clear();

		// then
		assertThatThrownBy(() ->
			repository.findByDate(LocalDate.of(2022, 01, 01)).orElseThrow(
				() -> new IllegalArgumentException()
			)).isInstanceOf(IllegalArgumentException.class);
	}

}