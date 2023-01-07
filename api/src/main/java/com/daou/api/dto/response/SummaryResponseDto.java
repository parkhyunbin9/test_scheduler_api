package com.daou.api.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.daou.api.model.DailySummary;
import com.daou.api.model.HourlySummary;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;
import lombok.Getter;

public class SummaryResponseDto {

	@Getter
	public static class Hourly {

		private Integer hour;
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
		private BigDecimal maxSalesAmount;
		private BigDecimal maxCost;

		private Long minNewUser;
		private Long minChurnUser;
		private BigDecimal minPayAmount;
		private BigDecimal minSalesAmount;
		private BigDecimal minCost;

		@Builder
		@QueryProjection
		public Hourly(Integer hour, Long sumNewUser, Long sumChurnUser, BigDecimal sumPayAmount,
			BigDecimal sumCost, BigDecimal sumSalesAmount, Double avgNewUser,
			Double avgChurnUser, Double avgPayAmount, Double avgCost, Double avgSalesAmount,
			Long maxNewUser, Long maxChurnUser, BigDecimal maxPayAmount,
			BigDecimal maxSalesAmount, BigDecimal maxCost, Long minNewUser, Long minChurnUser,
			BigDecimal minPayAmount, BigDecimal minSalesAmount, BigDecimal minCost) {
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
			this.maxSalesAmount = maxSalesAmount;
			this.maxCost = maxCost;
			this.minNewUser = minNewUser;
			this.minChurnUser = minChurnUser;
			this.minPayAmount = minPayAmount;
			this.minSalesAmount = minSalesAmount;
			this.minCost = minCost;
		}

		public static Hourly fromEntity(HourlySummary hourlySummary) {
			return Hourly.builder()
				.hour(hourlySummary.getHour())
				.sumNewUser(hourlySummary.getSumNewUser())
				.sumChurnUser(hourlySummary.getSumChurnUser())
				.sumPayAmount(hourlySummary.getSumPayAmount())
				.sumCost(hourlySummary.getSumCost())
				.sumSalesAmount(hourlySummary.getSumSalesAmount())
				.avgNewUser(hourlySummary.getAvgNewUser())
				.avgChurnUser(hourlySummary.getAvgChurnUser())
				.avgPayAmount(hourlySummary.getAvgPayAmount())
				.avgCost(hourlySummary.getAvgCost())
				.avgSalesAmount(hourlySummary.getAvgSalesAmount())
				.maxNewUser(hourlySummary.getMaxNewUser())
				.maxChurnUser(hourlySummary.getMaxChurnUser())
				.maxPayAmount(hourlySummary.getMaxPayAmount())
				.maxCost(hourlySummary.getMaxCost())
				.maxSalesAmount(hourlySummary.getMaxSalesAmount())
				.minNewUser(hourlySummary.getMinNewUser())
				.minChurnUser(hourlySummary.getMinChurnUser())
				.minPayAmount(hourlySummary.getMinPayAmount())
				.minCost(hourlySummary.getMinCost())
				.minSalesAmount(hourlySummary.getMinSalesAmount())
				.build();
		}
	}

	@Getter
	public static class Daily {

		@JsonFormat(pattern = "yyyy-MM-dd")
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
		private BigDecimal maxSalesAmount;
		private BigDecimal maxCost;

		private Long minNewUser;
		private Long minChurnUser;
		private BigDecimal minPayAmount;
		private BigDecimal minSalesAmount;
		private BigDecimal minCost;

		@Builder
		@QueryProjection
		public Daily(LocalDate date, Long sumNewUser, Long sumChurnUser, BigDecimal sumPayAmount,
			BigDecimal sumCost, BigDecimal sumSalesAmount, Double avgNewUser,
			Double avgChurnUser, Double avgPayAmount, Double avgCost, Double avgSalesAmount,
			Long maxNewUser, Long maxChurnUser, BigDecimal maxPayAmount,
			BigDecimal maxSalesAmount, BigDecimal maxCost, Long minNewUser, Long minChurnUser,
			BigDecimal minPayAmount, BigDecimal minSalesAmount, BigDecimal minCost) {
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
			this.maxSalesAmount = maxSalesAmount;
			this.maxCost = maxCost;
			this.minNewUser = minNewUser;
			this.minChurnUser = minChurnUser;
			this.minPayAmount = minPayAmount;
			this.minSalesAmount = minSalesAmount;
			this.minCost = minCost;
		}

		public static Daily fromEntity(DailySummary dailySummary) {
			return Daily.builder()
				.date(dailySummary.getDate())
				.sumNewUser(dailySummary.getSumNewUser())
				.sumChurnUser(dailySummary.getSumChurnUser())
				.sumPayAmount(dailySummary.getSumPayAmount())
				.sumCost(dailySummary.getSumCost())
				.sumSalesAmount(dailySummary.getSumSalesAmount())
				.avgNewUser(dailySummary.getAvgNewUser())
				.avgChurnUser(dailySummary.getAvgChurnUser())
				.avgPayAmount(dailySummary.getAvgPayAmount())
				.avgCost(dailySummary.getAvgCost())
				.avgSalesAmount(dailySummary.getAvgSalesAmount())
				.maxNewUser(dailySummary.getMaxNewUser())
				.maxChurnUser(dailySummary.getMaxChurnUser())
				.maxPayAmount(dailySummary.getMaxPayAmount())
				.maxCost(dailySummary.getMaxCost())
				.maxSalesAmount(dailySummary.getMaxSalesAmount())
				.minNewUser(dailySummary.getMinNewUser())
				.minChurnUser(dailySummary.getMinChurnUser())
				.minPayAmount(dailySummary.getMinPayAmount())
				.minCost(dailySummary.getMinCost())
				.minSalesAmount(dailySummary.getMinSalesAmount())
				.build();
		}
	}
}
