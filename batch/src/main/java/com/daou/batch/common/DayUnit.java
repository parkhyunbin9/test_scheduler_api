package com.daou.batch.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DayUnit {

	ONE_DAY(1), ONE_WEEK(7), TWO_WEEK(14),
	ONE_MONTH(30), THREE_MONTH(90), SIX_MONTH(180),
	ONE_YEAR(365);

	public int days;
}
