package com.daou.api.repository;

import static com.daou.api.model.QHourlyData.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;

import com.daou.api.dto.request.search.SearchConditionDto;
import com.daou.api.dto.response.DailyResponseDto;
import com.daou.api.dto.response.HourlyResponseDto;
import com.daou.api.dto.response.QDailyResponseDto_DailyChurnUser;
import com.daou.api.dto.response.QDailyResponseDto_DailyCost;
import com.daou.api.dto.response.QDailyResponseDto_DailyNewUser;
import com.daou.api.dto.response.QDailyResponseDto_DailyPayAmount;
import com.daou.api.dto.response.QDailyResponseDto_DailySalesAmount;
import com.daou.api.dto.response.QHourlyResponseDto_HourlyChurnUser;
import com.daou.api.dto.response.QHourlyResponseDto_HourlyCost;
import com.daou.api.dto.response.QHourlyResponseDto_HourlyNewUser;
import com.daou.api.dto.response.QHourlyResponseDto_HourlyPayAmount;
import com.daou.api.dto.response.QHourlyResponseDto_HourlySalesAmount;
import com.daou.api.model.HourlyData;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HourlyDataRepositoryImpl extends QuerydslRepositorySupport implements HourlyDataRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	public HourlyDataRepositoryImpl(EntityManager em) {
		super(HourlyData.class);
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public Page<HourlyResponseDto.HourlyNewUser> findHourlyNewUserWithConditions(SearchConditionDto condition,
		Pageable pageable) {
		List<HourlyResponseDto.HourlyNewUser> contents =
			queryFactory
				.select(
					new QHourlyResponseDto_HourlyNewUser(hourlyData.hour, hourlyData.newUser))
				.from(hourlyData)
				.where(
					hourEq(condition),
					hourGoe(condition),
					hourLoe(condition),
					dateEq(condition),
					dateGoe(condition),
					dateLoe(condition)
				)
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();

		JPAQuery<Long> countQuery = getCount(condition);
		return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchOne);
	}

	@Override
	public Page<HourlyResponseDto.HourlyChurnUser> findHourlyChurnUserWithConditions(SearchConditionDto condition,
		Pageable pageable) {
		List<HourlyResponseDto.HourlyChurnUser> contents =
			queryFactory
				.select(
					new QHourlyResponseDto_HourlyChurnUser(hourlyData.hour, hourlyData.churnUser))
				.from(hourlyData)
				.where(
					hourEq(condition),
					hourGoe(condition),
					hourLoe(condition),
					dateEq(condition),
					dateGoe(condition),
					dateLoe(condition)
				)
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();

		JPAQuery<Long> countQuery = getCount(condition);
		return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchOne);
	}

	@Override
	public Page<HourlyResponseDto.HourlyPayAmount> findHourlyPayAmountWithConditions(SearchConditionDto condition,
		Pageable pageable) {
		List<HourlyResponseDto.HourlyPayAmount> contents =
			queryFactory
				.select(
					new QHourlyResponseDto_HourlyPayAmount(hourlyData.hour, hourlyData.payAmount))
				.from(hourlyData)
				.where(
					hourEq(condition),
					hourGoe(condition),
					hourLoe(condition),
					dateEq(condition),
					dateGoe(condition),
					dateLoe(condition)
				)
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();

		JPAQuery<Long> countQuery = getCount(condition);
		return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchOne);
	}

	@Override
	public Page<HourlyResponseDto.HourlyCost> findHourlyCostWithConditions(SearchConditionDto condition,
		Pageable pageable) {
		List<HourlyResponseDto.HourlyCost> contents =
			queryFactory
				.select(
					new QHourlyResponseDto_HourlyCost(hourlyData.hour, hourlyData.cost))
				.from(hourlyData)
				.where(
					hourEq(condition),
					hourGoe(condition),
					hourLoe(condition),
					dateEq(condition),
					dateGoe(condition),
					dateLoe(condition)
				)
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();

		JPAQuery<Long> countQuery = getCount(condition);
		return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchOne);

	}

	@Override
	public Page<HourlyResponseDto.HourlySalesAmount> findHourlySalesAmountWithConditions(SearchConditionDto condition,
		Pageable pageable) {
		List<HourlyResponseDto.HourlySalesAmount> contents =
			queryFactory
				.select(
					new QHourlyResponseDto_HourlySalesAmount(hourlyData.hour, hourlyData.salesAmount))
				.from(hourlyData)
				.where(
					hourEq(condition),
					hourGoe(condition),
					hourLoe(condition),
					dateEq(condition),
					dateGoe(condition),
					dateLoe(condition)
				)
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();

		JPAQuery<Long> countQuery = getCount(condition);
		return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchOne);
	}

	@Override
	public Page<DailyResponseDto.DailyNewUser> findDailyNewUserWithConditions(SearchConditionDto condition,
		Pageable pageable) {
		List<DailyResponseDto.DailyNewUser> contents =
			queryFactory
				.select(new QDailyResponseDto_DailyNewUser(
					hourlyData.date, hourlyData.newUser
				))
				.from(hourlyData)
				.where(
					hourEq(condition),
					hourGoe(condition),
					hourLoe(condition),
					dateEq(condition),
					dateGoe(condition),
					dateLoe(condition)
				)
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();

		JPAQuery<Long> countQuery = getCount(condition);
		return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchOne);
	}

	@Override
	public Page<DailyResponseDto.DailyChurnUser> findDailyChurnUserWithConditions(SearchConditionDto condition,
		Pageable pageable) {
		List<DailyResponseDto.DailyChurnUser> contents =
			queryFactory
				.select(new QDailyResponseDto_DailyChurnUser(
					hourlyData.date, hourlyData.churnUser
				))
				.from(hourlyData)
				.where(
					hourEq(condition),
					hourGoe(condition),
					hourLoe(condition),
					dateEq(condition),
					dateGoe(condition),
					dateLoe(condition)
				)
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();

		JPAQuery<Long> countQuery = getCount(condition);
		return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchOne);
	}

	@Override
	public Page<DailyResponseDto.DailyPayAmount> findDailyPayAmountWithConditions(SearchConditionDto condition,
		Pageable pageable) {
		List<DailyResponseDto.DailyPayAmount> contents =
			queryFactory
				.select(new QDailyResponseDto_DailyPayAmount(
					hourlyData.date, hourlyData.payAmount
				))
				.from(hourlyData)
				.where(
					hourEq(condition),
					hourGoe(condition),
					hourLoe(condition),
					dateEq(condition),
					dateGoe(condition),
					dateLoe(condition)
				)
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();

		JPAQuery<Long> countQuery = getCount(condition);
		return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchOne);
	}

	@Override
	public Page<DailyResponseDto.DailyCost> findDailyCostWithConditions(SearchConditionDto condition,
		Pageable pageable) {
		List<DailyResponseDto.DailyCost> contents =
			queryFactory
				.select(new QDailyResponseDto_DailyCost(
					hourlyData.date, hourlyData.cost
				))
				.from(hourlyData)
				.where(
					hourEq(condition),
					hourGoe(condition),
					hourLoe(condition),
					dateEq(condition),
					dateGoe(condition),
					dateLoe(condition)
				)
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();

		JPAQuery<Long> countQuery = getCount(condition);
		return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchOne);
	}

	@Override
	public Page<DailyResponseDto.DailySalesAmount> findDailySalesAmountWithConditions(SearchConditionDto condition,
		Pageable pageable) {
		List<DailyResponseDto.DailySalesAmount> contents =
			queryFactory
				.select(new QDailyResponseDto_DailySalesAmount(
					hourlyData.date, hourlyData.salesAmount
				))
				.from(hourlyData)
				.where(
					hourEq(condition),
					hourGoe(condition),
					hourLoe(condition),
					dateEq(condition),
					dateGoe(condition),
					dateLoe(condition)
				)
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();

		JPAQuery<Long> countQuery = getCount(condition);
		return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchOne);
	}

	private BooleanExpression hourEq(SearchConditionDto condition) {
		return (condition.getHour() != null) ? hourlyData.hour.eq(condition.getHour()) : null;
	}

	private BooleanExpression hourGoe(SearchConditionDto condition) {
		return (condition.getStartHour() != null) ? hourlyData.hour.goe(condition.getStartHour()) : null;
	}

	private BooleanExpression hourLoe(SearchConditionDto condition) {
		return (condition.getEndHour() != null) ? hourlyData.hour.loe(condition.getEndHour()) : null;
	}

	private BooleanExpression dateEq(SearchConditionDto condition) {
		return (Objects.nonNull(condition.getDate()) && condition.getDate().isBefore(LocalDate.now())) ?
			hourlyData.date.eq(condition.getDate()) : null;
	}

	private BooleanExpression dateGoe(SearchConditionDto condition) {
		return (Objects.nonNull(condition.getStartDate()) && condition.getStartDate().isBefore(LocalDate.now())) ?
			hourlyData.date.goe(condition.getStartDate()) : null;
	}

	private BooleanExpression dateLoe(SearchConditionDto condition) {
		return (Objects.nonNull(condition.getEndDate()) && condition.getEndDate().isBefore(LocalDate.now())) ?
			hourlyData.date.loe(condition.getEndDate()) : null;
	}

	private JPAQuery<Long> getCount(SearchConditionDto condition) {
		return queryFactory
			.select(hourlyData.count())
			.from(hourlyData)
			.where(
				hourEq(condition),
				hourGoe(condition),
				hourLoe(condition),
				dateEq(condition),
				dateGoe(condition),
				dateLoe(condition)
			);
	}

}
