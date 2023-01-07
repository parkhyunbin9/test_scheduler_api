package com.daou.batch.common.validation;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;

import lombok.extern.slf4j.Slf4j;
import com.daou.batch.dto.HourlyRowDataDto;

@Slf4j
public class RowDataDtoValidator implements Validator<HourlyRowDataDto> {

	@Override
	public void validate(HourlyRowDataDto value) throws ValidationException {
		if (value.getDate().isAfter(LocalDate.now().minusDays(1))) {
			log.error(value.getDate().toString());
			throw new ValidationException("유효하지 않은 날짜의 데이터입니다. 집계는 과거 데이터만 가능합니다.");
		}
		if (value.getHour() < 0 || value.getHour() > 23) {
			log.error(Integer.toString(value.getHour()));
			throw new ValidationException("유효하지 않은 시간의 데이터입니다.");
		}
		if (isSmallerThanZero(value.getNewUser())) {
			log.error(Long.toString(value.getNewUser()));
			throw new ValidationException("유효하지 않은 신규 유저수 데이터입니다.");
		}
		if (isSmallerThanZero(value.getChurnUser())) {
			log.error(Long.toString(value.getChurnUser()));
			throw new ValidationException("유효하지 않은 이탈 유저수 데이터입니다.");
		}
		if (isSmallerThanZero(value.getPayAmount())) {
			log.error(value.getPayAmount().toString());
			throw new ValidationException("유효하지 않은 결제 금액 데이터입니다.");
		}
		if (isSmallerThanZero(value.getCost())) {
			log.error(value.getCost().toString());
			throw new ValidationException("유효하지 않은 비용 데이터입니다.");
		}
		if (isSmallerThanZero(value.getSalesAmount())) {
			log.error(value.getSalesAmount().toString());
			throw new ValidationException("유효하지 않은 매출 데이터입니다.");
		}
	}

	private boolean isSmallerThanZero(Long value) {
		return value < 0;
	}

	private boolean isSmallerThanZero(BigDecimal value) {
		return value.compareTo(BigDecimal.ZERO) < 0;
	}
}
