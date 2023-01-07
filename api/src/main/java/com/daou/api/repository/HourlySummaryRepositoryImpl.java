package com.daou.api.repository;

import static com.daou.api.model.QHourlySummary.*;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.daou.api.dto.request.search.SearchConditionDto;
import com.daou.api.dto.response.QSummaryResponseDto_Hourly;
import com.daou.api.dto.response.SummaryResponseDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HourlySummaryRepositoryImpl extends QuerydslRepositorySupport implements HourlySummaryRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	public HourlySummaryRepositoryImpl(EntityManager em) {
		super(SummaryResponseDto.Hourly.class);
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public List<SummaryResponseDto.Hourly> findHourlySummaryWithCondition(SearchConditionDto condition) {
		return queryFactory
			.select(new QSummaryResponseDto_Hourly(
				hourlySummary.hour,

				hourlySummary.sumNewUser,
				hourlySummary.sumChurnUser,
				hourlySummary.sumPayAmount,
				hourlySummary.sumCost,
				hourlySummary.sumSalesAmount,

				hourlySummary.avgNewUser,
				hourlySummary.avgChurnUser,
				hourlySummary.avgPayAmount,
				hourlySummary.avgCost,
				hourlySummary.avgSalesAmount,

				hourlySummary.maxNewUser,
				hourlySummary.maxChurnUser,
				hourlySummary.maxPayAmount,
				hourlySummary.maxCost,
				hourlySummary.maxSalesAmount,

				hourlySummary.minNewUser,
				hourlySummary.minChurnUser,
				hourlySummary.minPayAmount,
				hourlySummary.minCost,
				hourlySummary.minSalesAmount
			)).from(hourlySummary)
			.where(
				hourEq(condition),
				hourGoe(condition),
				hourLoe(condition)
			)
			.fetch();
	}

	private BooleanExpression hourEq(SearchConditionDto condition) {
		return (condition.getHour() != null) ? hourlySummary.hour.eq(condition.getHour()) : null;
	}

	private BooleanExpression hourGoe(SearchConditionDto condition) {
		return (condition.getStartHour() != null) ? hourlySummary.hour.goe(condition.getStartHour()) : null;
	}

	private BooleanExpression hourLoe(SearchConditionDto condition) {
		return (condition.getEndHour() != null) ? hourlySummary.hour.loe(condition.getEndHour()) : null;
	}
}
