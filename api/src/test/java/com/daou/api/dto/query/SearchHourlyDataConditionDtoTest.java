package com.daou.api.dto.query;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.daou.api.controller.HourlyController;
import com.daou.api.dto.request.search.SearchConditionDto;
import com.daou.api.dto.response.HourlyResponseDto;
import com.daou.api.service.HourlyService;

@ExtendWith(MockitoExtension.class)
class SearchHourlyDataConditionDtoTest {

	@InjectMocks
	HourlyController hourlyController;

	@Mock
	HourlyService hourlyService;
	Pageable page = Pageable.ofSize(1);
	private MockMvc mock;

	@BeforeEach
	public void init() {
		mock = MockMvcBuilders.standaloneSetup(hourlyController)
			.setCustomArgumentResolvers(
				new PageableHandlerMethodArgumentResolver()
			).build();
	}

	@Nested
	@DisplayName("조회시간과 조회날짜")
	class HourAndDate {

		@DisplayName("시간의 범위는 0~23 사이")
		@Test
		void testValidationSearchTime() throws Exception {
			// given
			SearchConditionDto hourMinCond = new SearchConditionDto(0);
			SearchConditionDto hourMaxCond = new SearchConditionDto(23);
			SearchConditionDto hourUnderMinCond = new SearchConditionDto(-1);
			SearchConditionDto hourOverMaxCond = new SearchConditionDto(24);

			ArrayList<HourlyResponseDto.HourlyNewUser> mockData = new ArrayList<>();
			mockData.add(new HourlyResponseDto.HourlyNewUser(0, 1L));
			PageImpl<HourlyResponseDto.HourlyNewUser> hourlyNewUsers = new PageImpl<>(mockData, page, 1);

			when(hourlyService.getHourlyNewUser(any(SearchConditionDto.class), any(Pageable.class))).thenReturn(
				hourlyNewUsers);

			// when & then
			mock.perform(
					get("/api/hourly/new-user")
						.param("hour", hourMinCond.getHour().toString())
						.param("page", page.toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

			mock.perform(
					get("/api/hourly/new-user")
						.param("hour", hourMaxCond.getHour().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

			mock.perform(
					get("/api/hourly/new-user")
						.param("hour", hourUnderMinCond.getHour().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/hourly/new-user")
						.param("hour", hourOverMaxCond.getHour().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
		}

		@DisplayName("조회 날짜는 과거")
		@Test
		void testValidationSearchDate() throws Exception {
			// given
			SearchConditionDto yesterdayCond = new SearchConditionDto(LocalDate.now().minusDays(1));
			SearchConditionDto todayCond = new SearchConditionDto(LocalDate.now());
			SearchConditionDto tomorrowCond = new SearchConditionDto(LocalDate.now().plusDays(1));

			ArrayList<HourlyResponseDto.HourlyNewUser> mockData = new ArrayList<>();
			mockData.add(new HourlyResponseDto.HourlyNewUser(0, 1L));
			PageImpl<HourlyResponseDto.HourlyNewUser> hourlyNewUsers = new PageImpl<>(mockData, page, 1);

			when(hourlyService.getHourlyNewUser(any(SearchConditionDto.class), any(Pageable.class))).thenReturn(
				hourlyNewUsers);

			// when & then
			mock.perform(
					get("/api/hourly/new-user")
						.param("date", yesterdayCond.getDate().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

			mock.perform(
					get("/api/hourly/new-user")
						.param("date", todayCond.getDate().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/hourly/new-user")
						.param("date", tomorrowCond.getDate().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
		}
	}

	@Nested
	@DisplayName("조회시간 범위와 조회날짜 범위")
	class HourAndDateRange {

		@DisplayName("시간 범위 조회시 시작시간은 종료시간보다 크며 조회시간 값은 null")
		@Test
		void testValidationSearchHourRange() throws Exception {
			// given
			SearchConditionDto validHourRangeCond = new SearchConditionDto(0, 5);
			SearchConditionDto hourUnderMinCond = new SearchConditionDto(-1, 5);
			SearchConditionDto hourOverMaxCond = new SearchConditionDto(0, 24);
			SearchConditionDto hourInvalidRangeCond = new SearchConditionDto(5, 0);
			SearchConditionDto hourInvalidRangeWithHourCond = new SearchConditionDto(5, 0);
			hourInvalidRangeWithHourCond.setHour(5);
			SearchConditionDto hourInvalidStartRangeWithHourCond = new SearchConditionDto();
			hourInvalidStartRangeWithHourCond.setStartHour(0);
			hourInvalidStartRangeWithHourCond.setHour(1);

			ArrayList<HourlyResponseDto.HourlyNewUser> mockData = new ArrayList<>();
			mockData.add(new HourlyResponseDto.HourlyNewUser(0, 1L));
			PageImpl<HourlyResponseDto.HourlyNewUser> hourlyNewUsers = new PageImpl<>(mockData, page, 1);

			when(hourlyService.getHourlyNewUser(any(SearchConditionDto.class), any(Pageable.class))).thenReturn(
				hourlyNewUsers);

			// when & then
			mock.perform(
					get("/api/hourly/new-user")
						.param("startHour", validHourRangeCond.getStartHour().toString())
						.param("endHour", validHourRangeCond.getEndHour().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

			mock.perform(
					get("/api/hourly/new-user")
						.param("startHour", hourUnderMinCond.getStartHour().toString())
						.param("endHour", hourUnderMinCond.getEndHour().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/hourly/new-user")
						.param("startHour", hourOverMaxCond.getStartHour().toString())
						.param("endHour", hourOverMaxCond.getEndHour().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/hourly/new-user")
						.param("startHour", hourInvalidRangeCond.getStartHour().toString())
						.param("endHour", hourInvalidRangeCond.getEndHour().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/hourly/new-user")
						.param("startHour", hourInvalidRangeWithHourCond.getStartHour().toString())
						.param("endHour", hourInvalidRangeWithHourCond.getEndHour().toString())
						.param("hour", hourInvalidRangeWithHourCond.getHour().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/hourly/new-user")
						.param("startHour", hourInvalidStartRangeWithHourCond.getStartHour().toString())
						.param("hour", hourInvalidStartRangeWithHourCond.getHour().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

		}

		@DisplayName("날짜 범위 조회시 시작날짜는 종료날짜보다 크며 조회날짜 값은 null")
		@Test
		void testValidationSearchDateRange() throws Exception {
			// given
			LocalDate today = LocalDate.now();
			SearchConditionDto validDateRangeCond =
				new SearchConditionDto(today.minusDays(2), today.minusDays(1));
			SearchConditionDto startDateAndEndDateOverTodayCond =
				new SearchConditionDto(today, today.plusDays(1));
			SearchConditionDto endDateOverTodayCond
				= new SearchConditionDto(today.minusDays(2), today.plusDays(1));
			SearchConditionDto dateInvalidRangeCond
				= new SearchConditionDto(today.minusDays(1), today.minusDays(2));
			SearchConditionDto dateInvalidRangeWithDateCond
				= new SearchConditionDto(today.minusDays(1), today.minusDays(2));
			dateInvalidRangeWithDateCond.setDate(today.minusDays(5));

			SearchConditionDto dateInvalidRangeStartAndDateCond = new SearchConditionDto();
			dateInvalidRangeStartAndDateCond.setStartDate(today.minusDays(1));
			dateInvalidRangeStartAndDateCond.setDate(today);

			ArrayList<HourlyResponseDto.HourlyNewUser> mockData = new ArrayList<>();
			mockData.add(new HourlyResponseDto.HourlyNewUser(0, 1L));
			PageImpl<HourlyResponseDto.HourlyNewUser> hourlyNewUsers = new PageImpl<>(mockData, page, 1);

			when(hourlyService.getHourlyNewUser(any(SearchConditionDto.class), any(Pageable.class))).thenReturn(
				hourlyNewUsers);

			// when & then
			mock.perform(
					get("/api/hourly/new-user")
						.param("startDate", validDateRangeCond.getStartDate().toString())
						.param("endDate", validDateRangeCond.getEndDate().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

			mock.perform(
					get("/api/hourly/new-user")
						.param("startDate", startDateAndEndDateOverTodayCond.getStartDate().toString())
						.param("endDate", startDateAndEndDateOverTodayCond.getEndDate().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/hourly/new-user")
						.param("startDate", endDateOverTodayCond.getStartDate().toString())
						.param("endDate", endDateOverTodayCond.getEndDate().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/hourly/new-user")
						.param("startDate", dateInvalidRangeCond.getStartDate().toString())
						.param("endDate", dateInvalidRangeCond.getEndDate().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/hourly/new-user")
						.param("startDate", dateInvalidRangeWithDateCond.getStartDate().toString())
						.param("endDate", dateInvalidRangeWithDateCond.getEndDate().toString())
						.param("date", dateInvalidRangeWithDateCond.getDate().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/hourly/new-user")
						.param("startDate", dateInvalidRangeStartAndDateCond.getStartDate().toString())
						.param("date", dateInvalidRangeStartAndDateCond.getDate().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

		}
	}
}