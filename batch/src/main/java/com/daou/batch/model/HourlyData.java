package com.daou.batch.model;

import java.math.BigDecimal;
import java.time.LocalDate;

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
@Entity(name = "hourly_data")
public class HourlyData extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "data_id")
	private Long id;

	@Column(name = "aggregate_date")
	private LocalDate date;

	@Column(name = "hours")
	private int hour;

	@Column
	private Long newUser;

	@Column
	private Long churnUser;

	@Column
	private BigDecimal payAmount;

	@Column
	private BigDecimal cost;

	@Column
	private BigDecimal salesAmount;

	@Builder
	public HourlyData(LocalDate date, int hour, Long newUser, Long churnUser, BigDecimal payAmount, BigDecimal cost,
		BigDecimal salesAmount) {
		this.date = date;
		this.hour = hour;
		this.newUser = newUser;
		this.churnUser = churnUser;
		this.payAmount = payAmount;
		this.cost = cost;
		this.salesAmount = salesAmount;
	}
}
