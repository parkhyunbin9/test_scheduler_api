package com.daou.batch.common.validation;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.validator.ValidationException;

import com.daou.batch.dto.HourlyRowDataDto;

class RowDataDtoValidatorTest {

	HourlyRowDataDto testData;

	@BeforeEach
	public void init() {
		testData = HourlyRowDataDto.builder()
			.date(LocalDate.now().minusDays(1))
			.hour(0)
			.newUser(10L)
			.churnUser(100L)
			.payAmount(BigDecimal.valueOf(100))
			.cost(BigDecimal.valueOf(200))
			.salesAmount(BigDecimal.valueOf(300))
			.build();
	}

	@DisplayName("날짜는 과거")
	@Test
	void dateShouldPast() {
		RowDataDtoValidator validator = new RowDataDtoValidator();
		testData.setDate(LocalDate.now());
		assertThrows(ValidationException.class,
			() -> validator.validate(testData));

		testData.setDate(LocalDate.now().plusDays(1));
		assertThrows(ValidationException.class,
			() -> validator.validate(testData));
		testData.setDate(LocalDate.now().plusDays(-1));
		assertDoesNotThrow(() -> validator.validate(testData));
	}

	@DisplayName("시간 범위 0 ~ 23 ")
	@Test
	void hourRangeShouldIn0To23() {
		RowDataDtoValidator validator = new RowDataDtoValidator();
		testData.setHour(-1);
		assertThrows(ValidationException.class,
			() -> validator.validate(testData));

		testData.setHour(24);
		assertThrows(ValidationException.class,
			() -> validator.validate(testData));
		testData.setHour(0);
		assertDoesNotThrow(() -> validator.validate(testData));
		testData.setHour(23);
		assertDoesNotThrow(() -> validator.validate(testData));
	}

	@DisplayName("신규 유저수는 양수")
	@Test
	void newUserShouldBiggerThanZero() {
		RowDataDtoValidator validator = new RowDataDtoValidator();
		testData.setNewUser(-1L);
		assertThrows(ValidationException.class,
			() -> validator.validate(testData));

		testData.setNewUser(100L);
		assertDoesNotThrow(() -> validator.validate(testData));
	}

	@DisplayName("이탈 유저수는 양수")
	@Test
	void churnUserShouldBiggerThanZero() {
		RowDataDtoValidator validator = new RowDataDtoValidator();
		testData.setChurnUser(-1L);
		assertThrows(ValidationException.class,
			() -> validator.validate(testData));

		testData.setChurnUser(100L);
		assertDoesNotThrow(() -> validator.validate(testData));
	}

	@DisplayName("매출은 양수")
	@Test
	void payAmountSholudBiggerThanZero() {
		RowDataDtoValidator validator = new RowDataDtoValidator();
		testData.setPayAmount(BigDecimal.valueOf(-1L));
		assertThrows(ValidationException.class,
			() -> validator.validate(testData));

		testData.setPayAmount(BigDecimal.valueOf(100L));
		assertDoesNotThrow(() -> validator.validate(testData));
	}

	@DisplayName("비용은 양수")
	@Test
	void costShouldBiggerThanZero() {
		RowDataDtoValidator validator = new RowDataDtoValidator();
		testData.setCost(BigDecimal.valueOf(-1L));
		assertThrows(ValidationException.class,
			() -> validator.validate(testData));

		testData.setCost(BigDecimal.valueOf(100L));
		assertDoesNotThrow(() -> validator.validate(testData));
	}

	@DisplayName("판매금액은 양수")
	@Test
	void salesAmountShouldBiggerThanZero() {
		RowDataDtoValidator validator = new RowDataDtoValidator();
		testData.setSalesAmount(BigDecimal.valueOf(-1L));
		assertThrows(ValidationException.class,
			() -> validator.validate(testData));

		testData.setSalesAmount(BigDecimal.valueOf(100L));
		assertDoesNotThrow(() -> validator.validate(testData));
	}

}