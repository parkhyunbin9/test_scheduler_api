package com.daou.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import com.daou.api.model.DailySummary;

@Getter
public class DailySummaryDto {

	private LocalDate date;
	private Long sumNewUser;
	private Long sumChurnUser;
	private BigDecimal sumPayAmount;
	private BigDecimal sumCost;
	private BigDecimal sumSalesAmount;
	private Double avgNewUser;
	private Double avgChurnUser;
	private Double avgPayAmount;
	private Double avgCost;
	private Double avgSalesAmount;
	private Long maxNewUser;
	private Long maxChurnUser;
	private BigDecimal maxPayAmount;
	private BigDecimal maxCost;
	private BigDecimal maxSalesAmount;
	private Long minNewUser;
	private Long minChurnUser;
	private BigDecimal minPayAmount;
	private BigDecimal minCost;
	private BigDecimal minSalesAmount;

	@QueryProjection
	public DailySummaryDto(LocalDate date, Long sumNewUser, Long sumChurnUser,
		BigDecimal sumPayAmount,
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
