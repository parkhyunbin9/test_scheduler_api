package com.daou.api.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class DailyResponseDto {

	@Getter
	@NoArgsConstructor
	public static class DailyNewUser {

		@JsonFormat(pattern = "yyyy-MM-dd")
		private LocalDate date;
		private Long newUser;

		@QueryProjection
		public DailyNewUser(LocalDate date, Long newUser) {
			this.date = date;
			this.newUser = newUser;
		}
	}

	@Getter
	public static class DailyChurnUser {

		@JsonFormat(pattern = "yyyy-MM-dd")
		private LocalDate date;
		private Long churnUser;

		@QueryProjection
		public DailyChurnUser(LocalDate date, Long churnUser) {
			this.date = date;
			this.churnUser = churnUser;
		}
	}

	@Getter
	public static class DailyPayAmount {

		@JsonFormat(pattern = "yyyy-MM-dd")
		private LocalDate date;
		private BigDecimal payAmount;

		@QueryProjection
		public DailyPayAmount(LocalDate date, BigDecimal payAmount) {
			this.date = date;
			this.payAmount = payAmount;
		}
	}

	@Getter
	public static class DailyCost {

		@JsonFormat(pattern = "yyyy-MM-dd")
		private LocalDate date;
		private BigDecimal cost;

		@QueryProjection
		public DailyCost(LocalDate date, BigDecimal cost) {
			this.date = date;
			this.cost = cost;
		}
	}

	@Getter
	public static class DailySalesAmount {

		@JsonFormat(pattern = "yyyy-MM-dd")
		private LocalDate date;
		private BigDecimal salesAmount;

		@QueryProjection
		public DailySalesAmount(LocalDate date, BigDecimal salesAmount) {
			this.date = date;
			this.salesAmount = salesAmount;
		}
	}

}
