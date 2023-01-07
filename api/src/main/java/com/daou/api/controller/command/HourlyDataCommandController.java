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
import com.daou.api.dto.request.HourlyInfoRequestDto;
import com.daou.api.service.HourlyCommandService;
import com.daou.api.service.HourlyService;

@Slf4j
@Api(value = "날짜의 시간별 집계 데이터 삽입/수정/삭제 API")
@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class HourlyDataCommandController {

	private final HourlyCommandService hourlyService;

	@ApiOperation(value = "시간별 데이터 추가", notes = "날짜와 시간 기준 데이터 삽입")
	@PostMapping(value = "hourly-data", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public CommonResponse hourlyDataSave(@Validated @RequestBody HourlyInfoRequestDto.SaveRequest saveRequest) {
		return new CommonResponse(hourlyService.saveHourlyData(saveRequest));
	}

	@ApiOperation(value = "시간별 데이터 수정 ", notes = "날짜와 시간 기준 데이터 수정")
	@PutMapping(value = "hourly-data", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public CommonResponse hourlyDataModify(
		@Validated @RequestBody HourlyInfoRequestDto.UpdateRequest updateRequest) {
		return new CommonResponse(hourlyService.updateHourlyData(updateRequest));
	}

	@ApiOperation(value = "시간별 데이터 삭제", notes = "날짜와 시간 기준 데이터 삭제")
	@DeleteMapping(value = "hourly-data", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public CommonResponse hourlyDataRemove(
		@Validated @ModelAttribute HourlyInfoRequestDto.DeleteRequest deleteRequest) {
		hourlyService.deleteHourlyData(deleteRequest);
		return CommonResponse.builder().build();
	}

}
