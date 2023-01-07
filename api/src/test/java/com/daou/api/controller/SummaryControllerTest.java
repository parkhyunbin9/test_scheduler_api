package com.daou.api.controller;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.daou.api.common.spec.CommonException;
import com.daou.api.common.spec.ExceptionCode;
import com.daou.api.dto.request.search.SearchConditionDto;
import com.daou.api.dto.response.SummaryResponseDto;
import com.daou.api.service.SummaryService;

@ExtendWith(MockitoExtension.class)
class SummaryControllerTest {

	static final LocalDate TEST_DATE = LocalDate.of(2022, 12, 01);
	static SearchConditionDto hourGreaterThanStartCond = new SearchConditionDto();
	static SearchConditionDto hourLessThanEndCond = new SearchConditionDto();
	static SearchConditionDto invalidHourRangeWithStartCond = new SearchConditionDto(3);
	static SearchConditionDto invalidHourRangeWithEndCond = new SearchConditionDto(3);
	static SearchConditionDto dateGreaterThanStartCond = new SearchConditionDto();
	static SearchConditionDto dateLessThanEndCond = new SearchConditionDto();
	static SearchConditionDto invalidDateRangeWithStartCond = new SearchConditionDto(TEST_DATE);
	static SearchConditionDto invalidDateRangeWithEndCond = new SearchConditionDto(TEST_DATE);
	@InjectMocks
	SummaryController summaryController;
	@Mock
	SummaryService summaryService;
	// hourCondition - Success
	SearchConditionDto hourMinCond = new SearchConditionDto(0);
	SearchConditionDto hourMaxCond = new SearchConditionDto(23);
	SearchConditionDto hourRangeCond = new SearchConditionDto(3, 5);
	// hourCondition - Fail
	SearchConditionDto hourUnderMinCond = new SearchConditionDto(-1);
	SearchConditionDto hourOverMaxCond = new SearchConditionDto(24);
	SearchConditionDto invalidHourRangeCond = new SearchConditionDto(5, 3);
	// dateCondition - Success
	SearchConditionDto datePastCond = new SearchConditionDto(TEST_DATE);
	SearchConditionDto dateRangeCond = new SearchConditionDto(TEST_DATE.minusDays(2), TEST_DATE);
	// dateCondition - Fail
	SearchConditionDto datePresentCond = new SearchConditionDto(LocalDate.now());
	SearchConditionDto dateFutureCond = new SearchConditionDto(LocalDate.now().plusDays(1));
	SearchConditionDto invalidDateRangeCond = new SearchConditionDto(TEST_DATE, TEST_DATE.minusDays(2));
	Pageable page = Pageable.ofSize(1);
	private MockMvc mock;

	@BeforeAll
	public static void setUp() {
		hourGreaterThanStartCond.setStartHour(22);
		hourLessThanEndCond.setEndHour(2);

		invalidHourRangeWithStartCond.setStartHour(1);
		invalidHourRangeWithEndCond.setEndHour(5);

		dateGreaterThanStartCond.setStartDate(TEST_DATE.minusDays(2));
		dateLessThanEndCond.setEndDate(TEST_DATE.plusDays(2));

		invalidDateRangeWithStartCond.setStartDate(TEST_DATE.minusDays(2));
		invalidDateRangeWithEndCond.setEndDate(TEST_DATE.plusDays(2));
	}

	@BeforeEach
	public void init() {
		mock = MockMvcBuilders.standaloneSetup(summaryController)
			.setCustomArgumentResolvers(
				new PageableHandlerMethodArgumentResolver()
			).build();
	}

	@DisplayName("NotFound")
	@Test
	void notFound() throws Exception {

		assertThatThrownBy(() ->
			mock.perform(
				get("/api/summary/hourly")
					.param("hour", hourMinCond.getHour().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isNotFound())
		).hasCause(new CommonException(ExceptionCode.NOT_FOUND));

		ArrayList<SummaryResponseDto.Hourly> hourlySummary = new ArrayList<>();
		when(summaryService.getHourlySummary(any(SearchConditionDto.class))).thenReturn(hourlySummary);

		assertThatThrownBy(() ->
			mock.perform(
				get("/api/summary/hourly")
					.param("hour", hourMinCond.getHour().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isNotFound()))
			.hasCause(new CommonException(ExceptionCode.NOT_FOUND));

	}

	@Disabled
	@DisplayName("json타입만 처리할 수 있다.")
	@Test
	void requestConsumeJSON() throws Exception {

		ArrayList<SummaryResponseDto.Hourly> hourlySummary = new ArrayList<>();
		hourlySummary.add(SummaryResponseDto.Hourly.builder().build());
		when(summaryService.getHourlySummary(any(SearchConditionDto.class))).thenReturn(hourlySummary);

		mock.perform(
			get("/api/summary/hourly")
				.param("hour", hourMinCond.getHour().toString())
				.param("page", page.toString())
				.contentType(MediaType.APPLICATION_JSON)
		).andExpect(status().isOk());

		mock.perform(
			get("/api/summary/hourly")
				.param("hour", hourMinCond.getHour().toString())
				.param("page", page.toString())
				.contentType(MediaType.ALL_VALUE)
		).andExpect(status().isUnsupportedMediaType());

		mock.perform(
			get("/api/summary/hourly")
				.param("page", page.toString())
				.contentType(MediaType.APPLICATION_XML)
		).andExpect(status().isUnsupportedMediaType());

		mock.perform(
			get("/api/summary/hourly")
				.param("hour", hourMinCond.getHour().toString())
				.param("page", page.toString())
				.contentType(MediaType.TEXT_HTML_VALUE)
		).andExpect(status().isUnsupportedMediaType());

		mock.perform(
			get("/api/summary/hourly")
				.param("hour", hourMinCond.getHour().toString())
				.param("page", page.toString())
				.contentType(MediaType.MULTIPART_FORM_DATA)
		).andExpect(status().isUnsupportedMediaType());
	}

	@DisplayName("json으로 return 한다.")
	@Test
	void requestProduceJSON() throws Exception {
		// given
		ArrayList<SummaryResponseDto.Hourly> hourlySummary = new ArrayList<>();
		hourlySummary.add(SummaryResponseDto.Hourly.builder().build());
		when(summaryService.getHourlySummary(any(SearchConditionDto.class))).thenReturn(hourlySummary);

		// when
		String hourlySummaryResponseContentType = mock.perform(
			get("/api/summary/hourly")
				.param("hour", hourMinCond.getHour().toString())
				.param("page", page.toString())
				.contentType(MediaType.APPLICATION_JSON)
		).andReturn().getResponse().getContentType();

		// then
		assertThat(hourlySummaryResponseContentType).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
	}

	@Nested
	@DisplayName("시간 조건 요청 성공")
	class SearchByHourSuccess {

		@DisplayName("누적 시간 요약 조회")
		@Test
		void searchHourlySummaryByHour() throws Exception {

			ArrayList<SummaryResponseDto.Hourly> hourlySummary = new ArrayList<>();
			hourlySummary.add(SummaryResponseDto.Hourly.builder().build());
			when(summaryService.getHourlySummary(any(SearchConditionDto.class))).thenReturn(hourlySummary);

			mock.perform(
				get("/api/summary/hourly")
					.param("hour", hourMinCond.getHour().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());

			mock.perform(
				get("/api/summary/hourly")
					.param("hour", hourMaxCond.getHour().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());

			mock.perform(
				get("/api/summary/hourly")
					.param("startHour", hourRangeCond.getStartHour().toString())
					.param("endHour", hourRangeCond.getEndHour().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());

			mock.perform(
				get("/api/summary/hourly")
					.param("startHour", hourGreaterThanStartCond.getStartHour().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());

			mock.perform(
				get("/api/summary/hourly")
					.param("endHour", hourLessThanEndCond.getEndHour().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());

		}

		@DisplayName("누적 일별 요약 조회")
		@Test
		void searchDailySummaryByHour() throws Exception {

			ArrayList<SummaryResponseDto.Daily> dailySummary = new ArrayList<>();
			dailySummary.add(SummaryResponseDto.Daily.builder().build());
			when(summaryService.getDailySummary(any(SearchConditionDto.class))).thenReturn(dailySummary);

			mock.perform(
				get("/api/summary/daily")
					.param("hour", hourMinCond.getHour().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());

			mock.perform(
				get("/api/summary/daily")
					.param("hour", hourMaxCond.getHour().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());

			mock.perform(
				get("/api/summary/daily")
					.param("startHour", hourRangeCond.getStartHour().toString())
					.param("endHour", hourRangeCond.getEndHour().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());

			mock.perform(
				get("/api/summary/daily")
					.param("startHour", hourGreaterThanStartCond.getStartHour().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());

			mock.perform(
				get("/api/summary/daily")
					.param("endHour", hourLessThanEndCond.getEndHour().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());
		}
	}

	@Nested
	@DisplayName("시간 조건 요청 실패")
	class SearchSummaryByHourFail {

		@DisplayName("누적 시간 요약 조회")
		@Test
		void searchHourlySummaryByHourFail() throws Exception {

			mock.perform(
					get("/api/summary/hourly")
						.param("hour", hourUnderMinCond.getHour().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/summary/hourly")
						.param("hour", hourOverMaxCond.getHour().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/summary/hourly")
						.param("startHour", invalidHourRangeCond.getStartHour().toString())
						.param("endHour", invalidHourRangeCond.getEndHour().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/summary/hourly")
						.param("startHour", invalidHourRangeWithStartCond.getStartHour().toString())
						.param("hour", invalidHourRangeWithStartCond.getHour().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/summary/hourly")
						.param("hour", invalidHourRangeWithEndCond.getHour().toString())
						.param("endHour", invalidHourRangeWithEndCond.getEndHour().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

		}

		@DisplayName("누적 일별 요약 조회")
		@Test
		void searchDailySummaryByHourFail() throws Exception {

			mock.perform(
					get("/api/summary/daily")
						.param("hour", hourUnderMinCond.getHour().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/summary/daily")
						.param("hour", hourOverMaxCond.getHour().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/summary/daily")
						.param("startHour", invalidHourRangeCond.getStartHour().toString())
						.param("endHour", invalidHourRangeCond.getEndHour().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/summary/daily")
						.param("startHour", invalidHourRangeWithStartCond.getStartHour().toString())
						.param("hour", invalidHourRangeWithStartCond.getHour().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/summary/daily")
						.param("hour", invalidHourRangeWithEndCond.getHour().toString())
						.param("endHour", invalidHourRangeWithEndCond.getEndHour().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
		}
	}

	@Nested
	@DisplayName("날짜 조건 요청 성공")
	class SearchByDateSuccess {

		@DisplayName("누적 시간 요약 조회")
		@Test
		void searchHourlySummaryByDate() throws Exception {

			ArrayList<SummaryResponseDto.Hourly> hourlySummary = new ArrayList<>();
			hourlySummary.add(SummaryResponseDto.Hourly.builder().build());
			when(summaryService.getHourlySummary(any(SearchConditionDto.class))).thenReturn(hourlySummary);

			mock.perform(
				get("/api/summary/hourly")
					.param("date", datePastCond.getDate().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());

			mock.perform(
				get("/api/summary/hourly")
					.param("startDate", dateRangeCond.getStartDate().toString())
					.param("endDate", dateRangeCond.getEndDate().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());

			mock.perform(
				get("/api/summary/hourly")
					.param("startDate", dateGreaterThanStartCond.getStartDate().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());

			mock.perform(
				get("/api/summary/hourly")
					.param("endDate", dateLessThanEndCond.getEndDate().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());

		}

		@DisplayName("누적 일별 요약 조회")
		@Test
		void searchDailySummaryByDate() throws Exception {

			ArrayList<SummaryResponseDto.Daily> dailySummary = new ArrayList<>();
			dailySummary.add(SummaryResponseDto.Daily.builder().build());
			when(summaryService.getDailySummary(any(SearchConditionDto.class))).thenReturn(dailySummary);

			mock.perform(
				get("/api/summary/daily")
					.param("date", datePastCond.getDate().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());

			mock.perform(
				get("/api/summary/daily")
					.param("startDate", dateRangeCond.getStartDate().toString())
					.param("endDate", dateRangeCond.getEndDate().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());

			mock.perform(
				get("/api/summary/daily")
					.param("startDate", dateGreaterThanStartCond.getStartDate().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());

			mock.perform(
				get("/api/summary/daily")
					.param("endDate", dateLessThanEndCond.getEndDate().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());
		}
	}

	@Nested
	@DisplayName("날짜 조건 요청 실패")
	class SearchByDateFail {

		@DisplayName("누적 시간 요약 조회")
		@Test
		void searchHourlySummaryByDateFail() throws Exception {

			mock.perform(
					get("/api/summary/hourly")
						.param("date", datePresentCond.getDate().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/summary/hourly")
						.param("date", dateFutureCond.getDate().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/summary/hourly")
						.param("startDate", invalidDateRangeCond.getStartDate().toString())
						.param("endDate", invalidDateRangeCond.getEndDate().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/summary/hourly")
						.param("startDate", invalidDateRangeWithStartCond.getStartDate().toString())
						.param("date", invalidDateRangeWithStartCond.getDate().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/summary/hourly")
						.param("date", invalidDateRangeWithEndCond.getDate().toString())
						.param("endDate", invalidDateRangeWithEndCond.getEndDate().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
		}

		@DisplayName("누적 일별 요약 조회")
		@Test
		void searchDailySummaryByDateFail() throws Exception {

			mock.perform(
					get("/api/summary/daily")
						.param("date", datePresentCond.getDate().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/summary/daily")
						.param("date", dateFutureCond.getDate().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/summary/daily")
						.param("startDate", invalidDateRangeCond.getStartDate().toString())
						.param("endDate", invalidDateRangeCond.getEndDate().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/summary/daily")
						.param("startDate", invalidDateRangeWithStartCond.getStartDate().toString())
						.param("date", invalidDateRangeWithStartCond.getDate().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/summary/daily")
						.param("date", invalidDateRangeWithEndCond.getDate().toString())
						.param("endDate", invalidDateRangeWithEndCond.getEndDate().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
		}
	}

}