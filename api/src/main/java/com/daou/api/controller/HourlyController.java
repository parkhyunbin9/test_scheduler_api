package com.daou.api.controller;

import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daou.api.common.spec.CommonException;
import com.daou.api.common.spec.CommonResponse;
import com.daou.api.common.spec.ExceptionCode;
import com.daou.api.dto.request.search.SearchConditionDto;
import com.daou.api.dto.response.HourlyResponseDto;
import com.daou.api.service.HourlyService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Api(value = "일 기준 시간대별 집계 조회 API")
@RestController
@RequestMapping("/api/hourly/")
@RequiredArgsConstructor
public class HourlyController {

	private final HourlyService hourlyService;

	@ApiOperation(value = "시간대별 신규 유저", notes = "조회일 기준 시간대별 신규 유저")
	@GetMapping(value = "new-user", produces = MediaType.APPLICATION_JSON_VALUE)
	public CommonResponse hourlyNewUserList(@Validated @ModelAttribute SearchConditionDto condition,
		Pageable pageable) {

		Page<HourlyResponseDto.HourlyNewUser> newUserList = hourlyService.getHourlyNewUser(condition, pageable);

		if (Objects.isNull(newUserList) || newUserList.isEmpty())
			throw new CommonException(ExceptionCode.NOT_FOUND);

		return new CommonResponse(newUserList);
	}

	@ApiOperation(value = "시간대별 이탈 유저", notes = "조회일 기준 시간대별 이탈 유저")
	@GetMapping(value = "churn-user", produces = MediaType.APPLICATION_JSON_VALUE)
	public CommonResponse hourlyChurnUserList(@Validated @ModelAttribute SearchConditionDto condition,
		Pageable pageable) {

		Page<HourlyResponseDto.HourlyChurnUser> churnUserList = hourlyService.getHourlyChurnUser(condition, pageable);

		if (Objects.isNull(churnUserList) || churnUserList.isEmpty())
			throw new CommonException(ExceptionCode.NOT_FOUND);

		return new CommonResponse(churnUserList);
	}

	@ApiOperation(value = "시간대별 매출", notes = "조회일 기준 시간대별 매출")
	@GetMapping(value = "pay-amount", produces = MediaType.APPLICATION_JSON_VALUE)
	public CommonResponse hourlyPayAmountUserList(@Validated @ModelAttribute SearchConditionDto condition,
		Pageable pageable) {

		Page<HourlyResponseDto.HourlyPayAmount> payAmountList = hourlyService.getHourlyPayAmount(condition, pageable);

		if (Objects.isNull(payAmountList) || payAmountList.isEmpty())
			throw new CommonException(ExceptionCode.NOT_FOUND);

		return new CommonResponse(payAmountList);
	}

	@ApiOperation(value = "시간대별 비용", notes = "조회일 기준 시간대별 비용")
	@GetMapping(value = "cost", produces = MediaType.APPLICATION_JSON_VALUE)
	public CommonResponse hourlyCostList(@Validated @ModelAttribute SearchConditionDto condition, Pageable pageable) {

		Page<HourlyResponseDto.HourlyCost> costList = hourlyService.getHourlyCost(condition, pageable);

		if (Objects.isNull(costList) || costList.isEmpty())
			throw new CommonException(ExceptionCode.NOT_FOUND);

		return new CommonResponse(costList);
	}

	@ApiOperation(value = "시간대별 판매 금액", notes = "조회일 기준 시간대별 판매 금액")
	@GetMapping(value = "sales-amount", produces = MediaType.APPLICATION_JSON_VALUE)
	public CommonResponse hourlySalesAmountList(@Validated @ModelAttribute SearchConditionDto condition,
		Pageable pageable) {

		Page<HourlyResponseDto.HourlySalesAmount> salesAmountList = hourlyService.getHourlySalesAmount(condition,
			pageable);

		if (Objects.isNull(salesAmountList) || salesAmountList.isEmpty())
			throw new CommonException(ExceptionCode.NOT_FOUND);

		return new CommonResponse(salesAmountList);
	}

}
