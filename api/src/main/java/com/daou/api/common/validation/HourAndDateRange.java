package com.daou.api.common.validation;

import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.daou.api.dto.request.search.SearchConditionValidator;

@Target({ElementType.TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = SearchConditionValidator.class)
@Documented
public @interface HourAndDateRange {
	String message() default "";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
