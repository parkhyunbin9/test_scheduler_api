package com.daou.api.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.PositiveOrZero;

import org.springframework.format.annotation.DateTimeFormat;

import com.daou.api.model.DailySummary;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class DailySummaryRequestDto {

	@Getter
	@NoArgsConstructor
	public static class SaveRequest {

		@NotNull
		@PastOrPresent
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private LocalDate date;

		@PositiveOrZero
		private Long sumNewUser;

		@PositiveOrZero
		private Long sumChurnUser;

		@PositiveOrZero
		private BigDecimal sumPayAmount;

		@PositiveOrZero
		private BigDecimal sumCost;

		@PositiveOrZero
		private BigDecimal sumSalesAmount;

		@PositiveOrZero
		private Double avgNewUser;

		@PositiveOrZero
		private Double avgChurnUser;

		@PositiveOrZero
		private Double avgPayAmount;

		@PositiveOrZero
		private Double avgCost;

		@PositiveOrZero
		private Double avgSalesAmount;

		@PositiveOrZero
		private Long maxNewUser;

		@PositiveOrZero
		private Long maxChurnUser;

		@PositiveOrZero
		private BigDecimal maxPayAmount;

		@PositiveOrZero
		private BigDecimal maxCost;

		@PositiveOrZero
		private BigDecimal maxSalesAmount;

		@PositiveOrZero
		private Long minNewUser;

		@PositiveOrZero
		private Long minChurnUser;

		@PositiveOrZero
		private BigDecimal minPayAmount;

		@PositiveOrZero
		private BigDecimal minCost;

		@PositiveOrZero
		private BigDecimal minSalesAmount;

		@Builder
		public SaveRequest(LocalDate date, Long sumNewUser, Long sumChurnUser,
			BigDecimal sumPayAmount, BigDecimal sumCost, BigDecimal sumSalesAmount,
			Double avgNewUser, Double avgChurnUser, Double avgPayAmount, Double avgCost,
			Double avgSalesAmount, Long maxNewUser, Long maxChurnUser, BigDecimal maxPayAmount,
			BigDecimal maxCost, BigDecimal maxSalesAmount, Long minNewUser, Long minChurnUser,
			BigDecimal minPayAmount, BigDecimal minCost, BigDecimal minSalesAmount) {
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
				.date(date)
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

	@Getter
	@NoArgsConstructor
	public static class UpdateRequest {

		@NotNull
		@PastOrPresent
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private LocalDate date;

		@PositiveOrZero
		private Long sumNewUser;

		@PositiveOrZero
		private Long sumChurnUser;

		@PositiveOrZero
		private BigDecimal sumPayAmount;

		@PositiveOrZero
		private BigDecimal sumCost;

		@PositiveOrZero
		private BigDecimal sumSalesAmount;

		@PositiveOrZero
		private Double avgNewUser;

		@PositiveOrZero
		private Double avgChurnUser;

		@PositiveOrZero
		private Double avgPayAmount;

		@PositiveOrZero
		private Double avgCost;

		@PositiveOrZero
		private Double avgSalesAmount;

		@PositiveOrZero
		private Long maxNewUser;

		@PositiveOrZero
		private Long maxChurnUser;

		@PositiveOrZero
		private BigDecimal maxPayAmount;

		@PositiveOrZero
		private BigDecimal maxCost;

		@PositiveOrZero
		private BigDecimal maxSalesAmount;

		@PositiveOrZero
		private Long minNewUser;

		@PositiveOrZero
		private Long minChurnUser;

		@PositiveOrZero
		private BigDecimal minPayAmount;

		@PositiveOrZero
		private BigDecimal minCost;

		@PositiveOrZero
		private BigDecimal minSalesAmount;

		@Builder
		public UpdateRequest(LocalDate date, Long sumNewUser, Long sumChurnUser,
			BigDecimal sumPayAmount, BigDecimal sumCost, BigDecimal sumSalesAmount,
			Double avgNewUser, Double avgChurnUser, Double avgPayAmount, Double avgCost,
			Double avgSalesAmount, Long maxNewUser, Long maxChurnUser, BigDecimal maxPayAmount,
			BigDecimal maxCost, BigDecimal maxSalesAmount, Long minNewUser, Long minChurnUser,
			BigDecimal minPayAmount, BigDecimal minCost, BigDecimal minSalesAmount) {
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
				.date(date)
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

	@Getter
	public static class DeleteRequest {

		@NotNull
		@PastOrPresent
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private LocalDate date;

		public DeleteRequest(LocalDate date) {
			this.date = date;
		}
	}
}
