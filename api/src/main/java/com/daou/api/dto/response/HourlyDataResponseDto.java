package com.daou.api.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Getter;
import com.daou.api.model.HourlyData;

public class HourlyDataResponseDto {

	@Getter
	public static class SaveAndUpdateResponse {

		@JsonFormat(pattern = "yyyy-MM-dd")
		private final LocalDate date;
		private final Integer hour;
		private final Long newUser;
		private final Long churnUser;
		private final BigDecimal payAmount;
		private final BigDecimal cost;
		private final BigDecimal salesAmount;

		@Builder
		public SaveAndUpdateResponse(LocalDate date, Integer hour, Long newUser, Long churnUser,
			BigDecimal payAmount, BigDecimal cost, BigDecimal salesAmount) {
			this.date = date;
			this.hour = hour;
			this.newUser = newUser;
			this.churnUser = churnUser;
			this.payAmount = payAmount;
			this.cost = cost;
			this.salesAmount = salesAmount;
		}

		public static SaveAndUpdateResponse fromEntity(HourlyData hourlyData) {
			return SaveAndUpdateResponse.builder()
				.date(hourlyData.getDate())
				.hour(hourlyData.getHour())
				.newUser(hourlyData.getNewUser())
				.churnUser(hourlyData.getChurnUser())
				.payAmount(hourlyData.getPayAmount())
				.cost(hourlyData.getCost())
				.salesAmount(hourlyData.getSalesAmount())
				.build();
		}

	}

}

