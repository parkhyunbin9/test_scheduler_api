package com.daou.api.dto.response;

import java.math.BigDecimal;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;

public class HourlyResponseDto {

	@Getter
	public static class HourlyNewUser {

		private int hour;
		private Long newUser;

		@QueryProjection
		public HourlyNewUser(int hour, Long newUser) {
			this.hour = hour;
			this.newUser = newUser;
		}
	}

	@Getter
	public static class HourlyChurnUser {

		private int hour;
		private Long churnUser;

		@QueryProjection
		public HourlyChurnUser(int hour, Long churnUser) {
			this.hour = hour;
			this.churnUser = churnUser;
		}
	}

	@Getter
	public static class HourlyPayAmount {

		private int hour;
		private BigDecimal payAmount;

		@QueryProjection
		public HourlyPayAmount(int hour, BigDecimal payAmount) {
			this.hour = hour;
			this.payAmount = payAmount;
		}
	}

	@Getter
	public static class HourlyCost {

		private int hour;
		private BigDecimal cost;

		@QueryProjection
		public HourlyCost(int hour, BigDecimal cost) {
			this.hour = hour;
			this.cost = cost;
		}
	}

	@Getter
	public static class HourlySalesAmount {

		private int hour;
		private BigDecimal salesAmount;

		@QueryProjection
		public HourlySalesAmount(int hour, BigDecimal salesAmount) {
			this.hour = hour;
			this.salesAmount = salesAmount;
		}
	}
}
