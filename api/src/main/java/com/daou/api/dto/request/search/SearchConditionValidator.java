package com.daou.api.dto.request.search;

import java.util.Objects;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Validator;

import com.daou.api.common.validation.HourAndDateRange;

public class SearchConditionValidator implements
	ConstraintValidator<HourAndDateRange, SearchConditionDto> {

	private Validator validator;

	public SearchConditionValidator(Validator validator) {
		this.validator = validator;
	}

	public SearchConditionValidator() {

	}

	@Override
	public void initialize(HourAndDateRange constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);
	}

	@Override
	public boolean isValid(SearchConditionDto value, ConstraintValidatorContext context) {
		boolean isValidHour = true;
		boolean isValidDate = true;

		if (isHourRange(value)) {
			if (!isValidHourRange(value)) {
				addErrorMessage(context, "조회시간 범위를 다시 확인해 주세요");
				isValidHour = false;
			}
		}

		if (isRangeAndHourValueSet(value)) {
			addErrorMessage(context, "조회시간 범위와 조회시간을 다시 확인해 주세요");
			isValidHour = false;
		}

		if (isDateRange(value)) {
			if (!isValidDateRange(value)) {
				addErrorMessage(context, "조회날짜 범위를 다시 확인해 주세요");
				isValidDate = false;
			}
		}

		if (isRangeAndDateValueSet(value)) {
			addErrorMessage(context, "조회날짜 범위와 조회날짜를 다시 확인해 주세요");
			isValidDate = false;
		}

		return isValidHour && isValidDate;
	}

	private boolean isHourRange(SearchConditionDto searchCond) {
		return Objects.nonNull(searchCond.getStartHour()) && Objects.nonNull(searchCond.getEndHour());
	}

	private boolean isValidHourRange(SearchConditionDto searchCond) {
		return ((searchCond.getEndHour() > searchCond.getStartHour()) && Objects.isNull(searchCond.getHour()));
	}

	private boolean isRangeAndHourValueSet(SearchConditionDto searchCond) {
		return Objects.nonNull(searchCond.getHour()) && (Objects.nonNull(searchCond.getStartHour()) ||
			Objects.nonNull(searchCond.getEndHour()));
	}

	private boolean isDateRange(SearchConditionDto searchCond) {
		return Objects.nonNull(searchCond.getStartDate()) && Objects.nonNull(searchCond.getEndDate());
	}

	private boolean isValidDateRange(SearchConditionDto searchCond) {
		return ((searchCond.getEndDate().isAfter(searchCond.getStartDate())) && Objects.isNull(searchCond.getDate()));
	}

	private boolean isRangeAndDateValueSet(SearchConditionDto searchCond) {
		return Objects.nonNull(searchCond.getDate()) && (Objects.nonNull(searchCond.getStartDate()) ||
			Objects.nonNull(searchCond.getEndDate()));
	}

	private void addErrorMessage(ConstraintValidatorContext context, String messageTemplate) {
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate(
			messageTemplate
		).addConstraintViolation();
	}
}


