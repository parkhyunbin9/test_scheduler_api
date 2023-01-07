package com.daou.batch.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "hourly_summary")
public class HourlySummary extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "summary_id")
	private Long id;

	@Column(name = "hours")
	private int hour;

	@Column
	private Long sumNewUser;

	@Column
	private Long sumChurnUser;

	@Column
	private BigDecimal sumPayAmount;

	@Column
	private BigDecimal sumCost;

	@Column
	private BigDecimal sumSalesAmount;

	@Column
	private Double avgNewUser;

	@Column
	private Double avgChurnUser;

	@Column
	private Double avgPayAmount;

	@Column
	private Double avgCost;

	@Column
	private Double avgSalesAmount;

	@Column
	private Long maxNewUser;

	@Column
	private Long maxChurnUser;

	@Column
	private BigDecimal maxPayAmount;

	@Column
	private BigDecimal maxCost;

	@Column
	private BigDecimal maxSalesAmount;

	@Column
	private Long minNewUser;

	@Column
	private Long minChurnUser;

	@Column
	private BigDecimal minPayAmount;

	@Column
	private BigDecimal minCost;

	@Column
	private BigDecimal minSalesAmount;

	@Builder
	public HourlySummary(int hour, Long sumNewUser, Long sumChurnUser, BigDecimal sumPayAmount, BigDecimal sumCost,
		BigDecimal sumSalesAmount, Double avgNewUser, Double avgChurnUser, Double avgPayAmount, Double avgCost,
		Double avgSalesAmount, Long maxNewUser, Long maxChurnUser, BigDecimal maxPayAmount, BigDecimal maxCost,
		BigDecimal maxSalesAmount, Long minNewUser, Long minChurnUser, BigDecimal minPayAmount, BigDecimal minCost,
		BigDecimal minSalesAmount) {
		this.hour = hour;
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
}
