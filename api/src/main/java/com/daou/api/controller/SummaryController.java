package com.daou.api.controller;

import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.daou.api.common.spec.CommonException;
import com.daou.api.common.spec.CommonResponse;
import com.daou.api.common.spec.ExceptionCode;
import com.daou.api.dto.request.search.SearchConditionDto;
import com.daou.api.dto.response.SummaryResponseDto;
import com.daou.api.service.SummaryService;

@Slf4j
@Api(value = "일별 & 시간별 누적 요약 조회 API")
@RestController
@RequestMapping("/api/summary/")
@RequiredArgsConstructor
public class SummaryController {

	private final SummaryService summaryService;

	@ApiOperation(value = "시간별 요약 데이터 조회", notes = "시간별 누적 유저 및 매출 관련 요약 데이터 조회")
	@GetMapping(value = "/hourly", produces = MediaType.APPLICATION_JSON_VALUE)
	public CommonResponse getHourlySummary(@Validated @ModelAttribute SearchConditionDto condition) {

		List<SummaryResponseDto.Hourly> hourlySummary = summaryService.getHourlySummary(condition);
		if (Objects.isNull(hourlySummary) || hourlySummary.isEmpty()) {
			throw new CommonException(ExceptionCode.NOT_FOUND);
		}
		return new CommonResponse(hourlySummary);
	}

	@ApiOperation(value = "일별 요약 데이터 조회", notes = "일별 누적 유저 및 매출 관련 요약 데이터 조회")
	@GetMapping(value = "/daily", produces = MediaType.APPLICATION_JSON_VALUE)
	public CommonResponse getDailySummary(@Validated @ModelAttribute SearchConditionDto condition) {
		List<SummaryResponseDto.Daily> dailySummary = summaryService.getDailySummary(condition);
		if (Objects.isNull(dailySummary) || dailySummary.isEmpty()) {
			throw new CommonException(ExceptionCode.NOT_FOUND);
		}
		return new CommonResponse(dailySummary);
	}

}
