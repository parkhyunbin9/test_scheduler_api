package com.daou.api.repository;

import static com.daou.api.model.QDailySummary.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.extern.slf4j.Slf4j;
import com.daou.api.dto.request.search.SearchConditionDto;
import com.daou.api.dto.response.QSummaryResponseDto_Daily;
import com.daou.api.dto.response.SummaryResponseDto;
import com.daou.api.model.DailySummary;

@Slf4j
public class DailySummaryRepositoryImpl extends QuerydslRepositorySupport implements DailySummaryRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	public DailySummaryRepositoryImpl(EntityManager em) {
		super(DailySummary.class);
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public List<SummaryResponseDto.Daily> findDailySummaryWithCondition(SearchConditionDto condition) {
		return queryFactory
			.select(new QSummaryResponseDto_Daily(
				dailySummary.date,

				dailySummary.sumNewUser,
				dailySummary.sumChurnUser,
				dailySummary.sumPayAmount,
				dailySummary.sumCost,
				dailySummary.sumSalesAmount,

				dailySummary.avgNewUser,
				dailySummary.avgChurnUser,
				dailySummary.avgPayAmount,
				dailySummary.avgCost,
				dailySummary.avgSalesAmount,

				dailySummary.maxNewUser,
				dailySummary.maxChurnUser,
				dailySummary.maxPayAmount,
				dailySummary.maxCost,
				dailySummary.maxSalesAmount,

				dailySummary.minNewUser,
				dailySummary.minChurnUser,
				dailySummary.minPayAmount,
				dailySummary.minCost,
				dailySummary.minSalesAmount
			)).from(dailySummary)
			.where(
				dateEq(condition),
				dateGoe(condition),
				dateLoe(condition)
			)
			.fetch();
	}

	private BooleanExpression dateEq(SearchConditionDto condition) {
		return (Objects.nonNull(condition.getDate()) && condition.getDate().isBefore(LocalDate.now())) ?
			dailySummary.date.eq(condition.getDate()) : null;
	}

	private BooleanExpression dateGoe(SearchConditionDto condition) {
		return (Objects.nonNull(condition.getStartDate()) && condition.getStartDate().isBefore(LocalDate.now())) ?
			dailySummary.date.goe(condition.getStartDate()) : null;
	}

	private BooleanExpression dateLoe(SearchConditionDto condition) {
		return (Objects.nonNull(condition.getEndDate()) && condition.getEndDate().isBefore(LocalDate.now())) ?
			dailySummary.date.loe(condition.getEndDate()) : null;
	}

}
