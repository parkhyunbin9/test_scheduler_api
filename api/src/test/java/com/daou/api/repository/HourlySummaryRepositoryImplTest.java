package com.daou.api.repository;

import static com.daou.api.model.QHourlySummary.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.util.BigDecimalComparator.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.daou.api.dto.request.HourlySummaryRequestDto;
import com.daou.api.dto.request.search.SearchConditionDto;
import com.daou.api.model.HourlySummary;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

@SpringBootTest
@Transactional
class HourlySummaryRepositoryImplTest {

	@Autowired
	HourlySummaryRepository repository;
	@Autowired
	EntityManager em;

	JPAQueryFactory queryFactory;

	@BeforeEach
	public void init() {
		List<HourlySummary> testData = IntStream.range(0, 23).mapToObj(i ->
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
		repository.saveAll(testData);
	}

	@Test
	void HourlySummary_시간_EQUAL_조회() throws NoSuchMethodException {

		// given
		queryFactory = new JPAQueryFactory(em);
		SearchConditionDto cond = new SearchConditionDto(12);
		SearchConditionDto noDateCond = new SearchConditionDto(23);
		HourlySummaryRepositoryImpl hourlySummaryRepository = new HourlySummaryRepositoryImpl(em);
		Method hourEq = hourlySummaryRepository.getClass().getDeclaredMethod("hourEq", SearchConditionDto.class);
		hourEq.setAccessible(true);
		try {
			// when
			List<HourlySummary> oneResult =
				queryFactory.selectFrom(hourlySummary)
					.where((BooleanExpression)hourEq.invoke(hourlySummaryRepository, cond))
					.fetch();

			List<HourlySummary> noResult =
				queryFactory.selectFrom(hourlySummary)
					.where((BooleanExpression)hourEq.invoke(hourlySummaryRepository, noDateCond))
					.fetch();

			// then

			assertThat(oneResult.size()).isEqualTo(1);
			assertThat(oneResult.get(0).getHour()).isEqualTo(12);
			assertThat(noResult.size()).isEqualTo(0);

		} catch (InvocationTargetException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	void HourlyData_시간_Goe_조회() throws NoSuchMethodException {

		// given
		final int START_TIME = 20;
		int timeDelta = 22 - START_TIME + 1;
		queryFactory = new JPAQueryFactory(em);
		SearchConditionDto hourGoeCondition = new SearchConditionDto();
		hourGoeCondition.setStartHour(START_TIME);
		HourlySummaryRepositoryImpl hourlySummaryRepository = new HourlySummaryRepositoryImpl(em);
		Method hourGoe = hourlySummaryRepository.getClass().getDeclaredMethod("hourGoe", SearchConditionDto.class);
		hourGoe.setAccessible(true);

		try {

			//when
			List<HourlySummary> searchResult = queryFactory.selectFrom(hourlySummary)
				.where((BooleanExpression)hourGoe.invoke(hourlySummaryRepository, hourGoeCondition))
				.fetch();

			//then
			assertThat(searchResult.size()).isEqualTo(timeDelta);
			assertThat(searchResult.get(0).getHour()).isBetween(START_TIME, 22);

		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	void HourlyData_시간_loe_조회() throws NoSuchMethodException {

		// given
		final int END_TIME = 5;
		int timeDelta = END_TIME + 1;
		queryFactory = new JPAQueryFactory(em);
		SearchConditionDto hourLoeCondition = new SearchConditionDto();
		hourLoeCondition.setEndHour(END_TIME);
		HourlySummaryRepositoryImpl hourlySummaryRepository = new HourlySummaryRepositoryImpl(em);
		Method hourLoe = hourlySummaryRepository.getClass().getDeclaredMethod("hourLoe", SearchConditionDto.class);
		hourLoe.setAccessible(true);

		try {

			//when
			List<HourlySummary> searchResult = queryFactory.selectFrom(hourlySummary)
				.where((BooleanExpression)hourLoe.invoke(hourlySummaryRepository, hourLoeCondition))
				.fetch();

			//then
			assertThat(searchResult.size()).isEqualTo(timeDelta);
			assertThat(searchResult.get(0).getHour()).isBetween(0, END_TIME);

		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	void HourlyData_시간_RANGE_조회() throws NoSuchMethodException {

		// given
		queryFactory = new JPAQueryFactory(em);
		final int START_TIME = 0;
		final int END_TIME = 10;
		int timeDelta = END_TIME - START_TIME + 1;
		SearchConditionDto hourRangeCondition = new SearchConditionDto(START_TIME, END_TIME);
		HourlySummaryRepositoryImpl hourlySummaryRepository = new HourlySummaryRepositoryImpl(em);

		Method hourGoe = hourlySummaryRepository.getClass().getDeclaredMethod("hourGoe", SearchConditionDto.class);
		hourGoe.setAccessible(true);
		Method hourLoe = hourlySummaryRepository.getClass().getDeclaredMethod("hourLoe", SearchConditionDto.class);
		hourLoe.setAccessible(true);

		try {
			//when
			List<HourlySummary> searchResult = queryFactory.selectFrom(hourlySummary)
				.where(
					(BooleanExpression)hourGoe.invoke(hourlySummaryRepository, hourRangeCondition),
					(BooleanExpression)hourLoe.invoke(hourlySummaryRepository, hourRangeCondition)
				)
				.fetch();

			//then
			assertThat(searchResult.size()).isEqualTo(timeDelta);
			assertThat(searchResult.get(0).getHour()).isBetween(START_TIME, END_TIME);
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	void 저장_hourlySummary() {
		// given
		HourlySummaryRequestDto.SaveRequest saveRequest = new HourlySummaryRequestDto.SaveRequest(0,
			0L, 0L, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
			0D, 0D, 0D, 0D, 0D,
			0L, 0L, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
			0L, 0L, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
		HourlySummary newHourlySummary = saveRequest.toEntity();

		// when
		HourlySummary savedData = repository.save(newHourlySummary);
		repository.flush();

		// then
		assertThat(savedData).isSameAs(newHourlySummary);
		assertThat(savedData)
			.usingComparatorForType(BIG_DECIMAL_COMPARATOR, BigDecimal.class)
			.usingRecursiveComparison()
			.isEqualTo(newHourlySummary);
	}

	@Test
	void 저장된_데이터_조회() {

		// given
		repository.deleteAll();
		HourlySummaryRequestDto.SaveRequest saveRequest = new HourlySummaryRequestDto.SaveRequest(22,
			1L, 1L, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
			1D, 1D, 1D, 1D, 1D,
			1L, 1L, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
			1L, 1L, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
		HourlySummary newHourlySummary = saveRequest.toEntity();
		HourlySummaryRequestDto.SaveRequest saveRequest2 = new HourlySummaryRequestDto.SaveRequest(23,
			10L, 10L, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE,
			10D, 10D, 10D, 10D, 10D,
			10L, 10L, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE,
			10L, 10L, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE);
		HourlySummary newHourlySummary2 = saveRequest2.toEntity();
		HourlySummary savedData = repository.save(newHourlySummary);
		HourlySummary savedData2 = repository.save(newHourlySummary2);
		repository.flush();
		em.clear();

		// when
		HourlySummary findData = repository.findByHour(22)
			.orElseThrow(() -> new IllegalArgumentException("해당 시간의 집계데이터가 없습니다."));
		HourlySummary findData2 = repository.findByHour(23)
			.orElseThrow(() -> new IllegalArgumentException("해당 시간의 집계데이터가 없습니다."));

		// then
		assertThat(savedData)
			.usingComparatorForType(BIG_DECIMAL_COMPARATOR, BigDecimal.class)
			.usingRecursiveComparison()
			.isEqualTo(newHourlySummary);

		assertThat(savedData2)
			.usingComparatorForType(BIG_DECIMAL_COMPARATOR, BigDecimal.class)
			.usingRecursiveComparison()
			.isEqualTo(newHourlySummary2);
	}

	@Test
	void 수정_hourlySummary() {
		// given
		repository.deleteAll();
		HourlySummaryRequestDto.SaveRequest saveRequest = new HourlySummaryRequestDto.SaveRequest(10,
			0L, 0L, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
			0D, 0D, 0D, 0D, 0D,
			0L, 0L, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
			0L, 0L, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
		HourlySummary newHourlySummary = saveRequest.toEntity();
		HourlySummary savedData = repository.save(newHourlySummary);
		HourlySummary beUpdateData = new HourlySummaryRequestDto.SaveRequest(11,
			1L, 1L, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN,
			1D, 1D, 1D, 1D, 1D,
			1L, 1L, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN,
			1L, 1L, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN).toEntity();
		repository.flush();
		em.clear();

		// when
		HourlySummary findData = repository.findByHour(10)
			.orElseThrow(() -> new IllegalArgumentException("요청하신 시간의 집계데이터가 없습니다."));
		findData.change(beUpdateData);
		repository.flush();

		HourlySummary updateData = repository.findByHour(10)
			.orElseThrow(() -> new IllegalArgumentException("요청하신 시간의 집계데이터가 없습니다."));

		//then
		assertThat(savedData.getId()).isEqualTo(updateData.getId());
		assertThat(savedData.getHour()).isEqualTo(updateData.getHour());

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

	}

	@Test
	void 삭제_hourlySummary() {

		// given
		repository.deleteAll();
		HourlySummaryRequestDto.SaveRequest saveRequest = new HourlySummaryRequestDto.SaveRequest(11,
			0L, 0L, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
			0D, 0D, 0D, 0D, 0D,
			0L, 0L, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
			0L, 0L, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
		HourlySummary newHourlySummary = saveRequest.toEntity();

		// when
		HourlySummary savedData = repository.save(newHourlySummary);
		repository.flush();

		HourlySummary findData = repository.findByHour(11).orElseThrow(() -> new IllegalArgumentException());
		repository.delete(findData);
		repository.flush();
		em.clear();

		// then
		assertThatThrownBy(() ->
			repository.findByHour(11).orElseThrow(
				() -> new IllegalArgumentException()
			)).isInstanceOf(IllegalArgumentException.class);
	}

}