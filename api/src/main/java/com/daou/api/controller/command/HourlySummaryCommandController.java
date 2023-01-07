package com.daou.api.controller.command;

import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.daou.api.common.spec.CommonResponse;
import com.daou.api.dto.request.DailySummaryRequestDto;
import com.daou.api.dto.request.HourlySummaryRequestDto;
import com.daou.api.service.SummaryCommandService;

@Slf4j
@Api(value = "누적 집계 데이터 삽입/수정/삭제 API")
@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class HourlySummaryCommandController {

	private final SummaryCommandService summaryService;

	@ApiOperation(value = "누적 시간 집계 추가", notes = "시간 값은 중복되면 안됩니다.")
	@PostMapping(value = "hourly-summary", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public CommonResponse hourlySummarySave(@Validated @RequestBody HourlySummaryRequestDto.SaveRequest saveRequest) {
		return new CommonResponse(summaryService.saveHourlySummary(saveRequest));
	}

	@ApiOperation(value = "누적 시간 집계 수정")
	@PutMapping(value = "hourly-summary", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public CommonResponse hourlySummaryModify(
		@Validated @RequestBody HourlySummaryRequestDto.UpdateRequest updateRequest) {
		return new CommonResponse(summaryService.updateHourlySummary(updateRequest));
	}

	@ApiOperation(value = "누적 시간 집계 삭제")
	@DeleteMapping(value = "hourly-summary", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public CommonResponse hourlySummaryRemove(
		@Validated @ModelAttribute HourlySummaryRequestDto.DeleteRequest deleteRequest) {
		summaryService.deleteHourlySummary(deleteRequest);
		return CommonResponse.builder().build();
	}

	@ApiOperation(value = "날짜별 집계 추가", notes = "날짜 값은 중복되면 안됩니다.")
	@PostMapping(value = "daily-summary", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public CommonResponse dailySummarySave(@Validated @RequestBody DailySummaryRequestDto.SaveRequest saveRequest) {
		return new CommonResponse(summaryService.saveDailySummary(saveRequest));

	}

	@ApiOperation(value = "날짜별 누적 집계 수정")
	@PutMapping(value = "daily-summary", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public CommonResponse dailySummaryModify(
		@Validated @RequestBody DailySummaryRequestDto.UpdateRequest updateRequest) {
		return new CommonResponse(summaryService.updateDailySummary(updateRequest));
	}

	@ApiOperation(value = "날짜별 누적 집계 삭제")
	@DeleteMapping(value = "daily-summary", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public CommonResponse dailySummaryRemove(
		@Validated @ModelAttribute DailySummaryRequestDto.DeleteRequest deleteRequest) {
		summaryService.deleteDailySummary(deleteRequest);
		return CommonResponse.builder().build();
	}
}
