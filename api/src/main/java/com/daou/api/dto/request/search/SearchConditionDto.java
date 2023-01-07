package com.daou.api.dto.request.search;

import java.time.LocalDate;

import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import com.daou.api.common.validation.HourAndDateRange;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@HourAndDateRange
@NoArgsConstructor
public class SearchConditionDto {
	@Range(min = 0, max = 23)
	private Integer hour;
	@Range(min = 0, max = 23)
	private Integer startHour;
	@Range(min = 0, max = 23)
	private Integer endHour;

	@Past
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;

	@Past
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate startDate;

	@Past
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate endDate;

	public SearchConditionDto(Integer hour) {
		this.hour = hour;
	}

	public SearchConditionDto(Integer startHour, Integer endHour) {
		this.startHour = startHour;
		this.endHour = endHour;
	}

	public SearchConditionDto(LocalDate startDate, LocalDate endDate) {
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public SearchConditionDto(LocalDate date) {
		this.date = date;
	}

	public SearchConditionDto(Integer hour, LocalDate date) {
		this.hour = hour;
		this.date = date;
	}

	public SearchConditionDto(Integer startHour, Integer endHour, LocalDate date) {
		this.startHour = startHour;
		this.endHour = endHour;
		this.date = date;
	}

	public SearchConditionDto(Integer hour, LocalDate startDate, LocalDate endDate) {
		this.hour = hour;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public SearchConditionDto(Integer startHour, Integer endHour, LocalDate startDate,
		LocalDate endDate) {
		this.startHour = startHour;
		this.endHour = endHour;
		this.startDate = startDate;
		this.endDate = endDate;
	}

}


