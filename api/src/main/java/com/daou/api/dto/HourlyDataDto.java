package com.daou.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.daou.api.model.HourlyData;

import lombok.Builder;
import lombok.Getter;

@Getter
public class HourlyDataDto {

	private LocalDate date;
	private int hour;
	private Long newUser;
	private Long churnUser;
	private BigDecimal payAmount;
	private BigDecimal cost;
	private BigDecimal salesAmount;
	private LocalDate createdDate;
	private LocalDate lastModifiedDate;

	@Builder
	public HourlyDataDto(LocalDate date, int hour, Long newUser, Long churnUser,
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
			.date(this.date)
			.hour(this.hour)
			.newUser(this.newUser)
			.churnUser(this.churnUser)
			.payAmount(this.payAmount)
			.cost(this.cost)
			.salesAmount(this.salesAmount)
			.build();
	}
}
