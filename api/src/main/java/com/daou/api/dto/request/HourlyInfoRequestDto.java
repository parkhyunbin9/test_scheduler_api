package com.daou.api.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.PositiveOrZero;

import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import com.daou.api.model.HourlyData;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class HourlyInfoRequestDto {

	@Getter
	@NoArgsConstructor
	public static class SaveRequest {

		@NotNull
		@PastOrPresent
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private LocalDate date;

		@NotNull
		@Range(min = 0, max = 23)
		private Integer hour;

		@PositiveOrZero
		private Long newUser;

		@PositiveOrZero
		private Long churnUser;

		@PositiveOrZero
		private BigDecimal payAmount;

		@PositiveOrZero
		private BigDecimal cost;

		@PositiveOrZero
		private BigDecimal salesAmount;

		@Builder
		public SaveRequest(LocalDate date, Integer hour, Long newUser, Long churnUser,
			BigDecimal payAmount, BigDecimal cost, BigDecimal salesAmount) {
			this.date = date;
			this.hour = hour;
			this.newUser = newUser;
			this.churnUser = churnUser;
			this.payAmount = payAmount;
			this.cost = cost;
			this.salesAmount = salesAmount;
		}

		public HourlyData toEntity() {
			return HourlyData.builder()
				.date(date)
				.hour(hour)
				.newUser(newUser)
				.churnUser(churnUser)
				.payAmount(payAmount)
				.cost(cost)
				.salesAmount(salesAmount)
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

		@NotNull
		@Range(min = 0, max = 23)
		private Integer hour;

		@PositiveOrZero
		private Long newUser;

		@PositiveOrZero
		private Long churnUser;

		@PositiveOrZero
		private BigDecimal payAmount;

		@PositiveOrZero
		private BigDecimal cost;

		@PositiveOrZero
		private BigDecimal salesAmount;

		@Builder
		public UpdateRequest(LocalDate date, Integer hour, Long newUser, Long churnUser,
			BigDecimal payAmount, BigDecimal cost, BigDecimal salesAmount) {
			this.date = date;
			this.hour = hour;
			this.newUser = newUser;
			this.churnUser = churnUser;
			this.payAmount = payAmount;
			this.cost = cost;
			this.salesAmount = salesAmount;
		}

		public HourlyData toEntity() {
			return HourlyData.builder()
				.newUser(this.newUser)
				.churnUser(this.churnUser)
				.payAmount(this.payAmount)
				.cost(this.cost)
				.salesAmount(this.salesAmount)
				.build();
		}
	}

	@Getter
	public static class DeleteRequest {

		@NotNull
		@PastOrPresent
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private LocalDate date;

		@NotNull
		@Range(min = 0, max = 23)
		private Integer hour;

		@Builder
		public DeleteRequest(LocalDate date, Integer hour) {
			this.date = date;
			this.hour = hour;
		}
	}

}
