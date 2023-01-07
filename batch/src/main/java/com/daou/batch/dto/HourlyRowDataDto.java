package com.daou.batch.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import com.daou.batch.model.HourlyRowData;

@Getter
@Setter
public class HourlyRowDataDto {

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
	public HourlyRowDataDto(LocalDate date, int hour, Long newUser, Long churnUser, BigDecimal payAmount,
		BigDecimal cost, BigDecimal salesAmount) {
		this.date = date;
		this.hour = hour;
		this.newUser = newUser;
		this.churnUser = churnUser;
		this.payAmount = payAmount;
		this.cost = cost;
		this.salesAmount = salesAmount;
	}

	public HourlyRowData toEntity() {
		return HourlyRowData.builder()
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

