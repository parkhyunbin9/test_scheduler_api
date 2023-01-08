package com.daou.batch.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.daou.batch.model.DailySummary;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;

@Getter
public class DailySummaryDto {

	private final LocalDate date;
	private final Long sumNewUser;
	private final Long sumChurnUser;
	private final BigDecimal sumPayAmount;
	private final BigDecimal sumCost;
	private final BigDecimal sumSalesAmount;
	private final Double avgNewUser;
	private final Double avgChurnUser;
	private final Double avgPayAmount;
	private final Double avgCost;
	private final Double avgSalesAmount;
	private final Long maxNewUser;
	private final Long maxChurnUser;
	private final BigDecimal maxPayAmount;
	private final BigDecimal maxCost;
	private final BigDecimal maxSalesAmount;
	private final Long minNewUser;
	private final Long minChurnUser;
	private final BigDecimal minPayAmount;
	private final BigDecimal minCost;
	private final BigDecimal minSalesAmount;

	@QueryProjection
	public DailySummaryDto(LocalDate date, Long sumNewUser, Long sumChurnUser, BigDecimal sumPayAmount,
		BigDecimal sumCost, BigDecimal sumSalesAmount, Double avgNewUser, Double avgChurnUser,
		Double avgPayAmount, Double avgCost, Double avgSalesAmount,
		Long maxNewUser, Long maxChurnUser, BigDecimal maxPayAmount,
		BigDecimal maxCost, BigDecimal maxSalesAmount, Long minNewUser,
		Long minChurnUser, BigDecimal minPayAmount, BigDecimal minCost,
		BigDecimal minSalesAmount) {
		this.date = date;
		this.sumNewUser = sumNewUser;
		this.sumChurnUser = sumChurnUser;
		this.sumPayAmount = sumPayAmount;
		this.sumCost = sumCost;
		this.sumSalesAmount = sumSalesAmount;
		this.avgNewUser = avgNewUser;
		this.avgChurnUser = avgChurnUser;
		this.avgPayAmount = avgPayAmount;
		this.avgCost = avgCost;
		this.avgSalesAmount = avgSalesAmount;
		this.maxNewUser = maxNewUser;
		this.maxChurnUser = maxChurnUser;
		this.maxPayAmount = maxPayAmount;
		this.maxCost = maxCost;
		this.maxSalesAmount = maxSalesAmount;
		this.minNewUser = minNewUser;
		this.minChurnUser = minChurnUser;
		this.minPayAmount = minPayAmount;
		this.minCost = minCost;
		this.minSalesAmount = minSalesAmount;
	}

	public DailySummary toEntity() {
		return DailySummary.builder()
			.date(this.date)
			.sumNewUser(this.sumNewUser)
			.sumChurnUser(this.sumChurnUser)
			.sumPayAmount(this.sumPayAmount)
			.sumCost(this.sumCost)
			.sumSalesAmount(this.sumSalesAmount)
			.avgNewUser(this.avgNewUser)
			.avgChurnUser(this.avgChurnUser)
			.avgPayAmount(this.avgPayAmount)
			.avgCost(this.avgCost)
			.avgSalesAmount(this.avgSalesAmount)
			.maxNewUser(this.maxNewUser)
			.maxChurnUser(this.maxChurnUser)
			.maxPayAmount(this.maxPayAmount)
			.maxCost(this.maxCost)
			.maxSalesAmount(this.maxSalesAmount)
			.minNewUser(this.minNewUser)
			.minChurnUser(this.minChurnUser)
			.minPayAmount(this.minPayAmount)
			.minCost(this.minCost)
			.minSalesAmount(this.minSalesAmount)
			.build();
	}
}
