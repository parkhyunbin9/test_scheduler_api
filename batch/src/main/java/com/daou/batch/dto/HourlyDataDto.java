package com.daou.batch.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.daou.batch.model.HourlyData;

import lombok.Builder;
import lombok.Getter;

@Getter
public class HourlyDataDto {

	private final LocalDate date;
	private final int hour;
	private final Long newUser;
	private final Long churnUser;
	private final BigDecimal payAmount;
	private final BigDecimal cost;
	private final BigDecimal salesAmount;
	private LocalDate createdDate;
	private LocalDate lastModifiedDate;

	@Builder
	public HourlyDataDto(LocalDate date, int hour, Long newUser, Long churnUser, BigDecimal payAmount, BigDecimal cost,
		BigDecimal salesAmount) {
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
