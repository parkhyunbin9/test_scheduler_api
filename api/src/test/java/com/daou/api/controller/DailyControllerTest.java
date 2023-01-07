package com.daou.api.controller;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindException;

import com.daou.api.common.spec.CommonException;
import com.daou.api.common.spec.ExceptionCode;
import com.daou.api.dto.request.search.SearchConditionDto;
import com.daou.api.dto.response.DailyResponseDto;
import com.daou.api.service.DailyService;

@ExtendWith(MockitoExtension.class)
class DailyControllerTest {

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
	DailyController dailyController;
	@Mock
	DailyService dailyService;
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
	Pageable page = PageRequest.ofSize(1);
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
		mock = MockMvcBuilders.standaloneSetup(dailyController)
			.setCustomArgumentResolvers(
				new PageableHandlerMethodArgumentResolver()
			).build();
	}

	@DisplayName("Not Found")
	@Test
	void notFound() throws Exception {

        // service return null
		assertThatThrownBy(() ->
			mock.perform(
					get("/api/daily/new-user")
						.param("hour", hourMinCond.getHour().toString())
						.param("page", page.toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
		).hasCause(new CommonException(ExceptionCode.NOT_FOUND));
        ArrayList<DailyResponseDto.DailyNewUser> mockData = new ArrayList<>();
        Page<DailyResponseDto.DailyNewUser> dailyNewUsers = new PageImpl<>(mockData, page, 1);

        when(dailyService.getDailyNewUser(any(SearchConditionDto.class), any(Pageable.class))).thenReturn(
            dailyNewUsers);

        // service return empty
		assertThatThrownBy(() ->
			mock.perform(
				get("/api/daily/new-user")
					.param("hour", hourMinCond.getHour().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
		).hasCause(new CommonException(ExceptionCode.NOT_FOUND));
	}

	@Disabled
	@DisplayName("json타입만 처리할 수 있다.")
	@Test
	void requestConsumeJSON() throws Exception {

		ArrayList<DailyResponseDto.DailyNewUser> mockData = new ArrayList<>();
		mockData.add(new DailyResponseDto.DailyNewUser(TEST_DATE, 1L));
		Page<DailyResponseDto.DailyNewUser> dailyNewUsers = new PageImpl<>(mockData, page, 1);

		when(dailyService.getDailyNewUser(any(SearchConditionDto.class), any(Pageable.class))).thenReturn(
			dailyNewUsers);

		mock.perform(
			get("/api/daily/new-user")
				.param("hour", hourMinCond.getHour().toString())
				.param("page", page.toString())
				.contentType(MediaType.APPLICATION_JSON)
		).andExpect(status().isOk());

		mock.perform(
			get("/api/daily/new-user")
				.param("hour", hourMinCond.getHour().toString())
				.param("page", page.toString())
				.contentType(MediaType.ALL_VALUE)
		).andExpect(status().isUnsupportedMediaType());

		mock.perform(
			get("/api/daily/new-user")
				.param("hour", hourMinCond.getHour().toString())
				.param("page", page.toString())
				.contentType(MediaType.APPLICATION_XML)
		).andExpect(status().isUnsupportedMediaType());

		mock.perform(
			get("/api/daily/new-user")
				.param("hour", hourMinCond.getHour().toString())
				.param("page", page.toString())
				.contentType(MediaType.TEXT_HTML_VALUE)
		).andExpect(status().isUnsupportedMediaType());

		mock.perform(
			get("/api/daily/new-user")
				.param("hour", hourMinCond.getHour().toString())
				.param("page", page.toString())
				.contentType(MediaType.MULTIPART_FORM_DATA)
		).andExpect(status().isUnsupportedMediaType());
	}

	@DisplayName("json으로 return 한다.")
	@Test
	void requestProduceJSON() throws Exception {

		// given
		ArrayList<DailyResponseDto.DailyNewUser> mockData = new ArrayList<>();
		mockData.add(new DailyResponseDto.DailyNewUser(TEST_DATE, 1L));
		Page<DailyResponseDto.DailyNewUser> dailyNewUsers = new PageImpl<>(mockData, page, 1);

		when(dailyService.getDailyNewUser(any(SearchConditionDto.class), any(Pageable.class))).thenReturn(
			dailyNewUsers);

		// when
		String newUserResponseContentType = mock.perform(
			get("/api/daily/new-user")
				.param("hour", hourMinCond.getHour().toString())
				.param("page", page.toString())
				.contentType(MediaType.APPLICATION_JSON)
		).andReturn().getResponse().getContentType();

		// then
		assertThat(newUserResponseContentType).isEqualTo(MediaType.APPLICATION_JSON_VALUE);

	}

	@Nested
	@DisplayName("시간 조건 요청 성공")
	class SearchByHourSuccess {

		@DisplayName("신규 유저")
		@Test
		void searchDailyNewUserByHour() throws Exception {

			ArrayList<DailyResponseDto.DailyNewUser> mockData = new ArrayList<>();
			mockData.add(new DailyResponseDto.DailyNewUser(TEST_DATE, 1L));
			Page<DailyResponseDto.DailyNewUser> dailyNewUsers = new PageImpl<>(mockData, page, 1);

			when(dailyService.getDailyNewUser(any(SearchConditionDto.class), any(Pageable.class))).thenReturn(
				dailyNewUsers);

			mock.perform(
				get("/api/daily/new-user")
					.param("hour", hourMinCond.getHour().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());

			mock.perform(
				get("/api/daily/new-user")
					.param("hour", hourMaxCond.getHour().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());

			mock.perform(
				get("/api/daily/new-user")
					.param("startHour", hourRangeCond.getStartHour().toString())
					.param("endHour", hourRangeCond.getEndHour().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());

			mock.perform(
				get("/api/daily/new-user")
					.param("startHour", hourGreaterThanStartCond.getStartHour().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());

			mock.perform(
				get("/api/daily/new-user")
					.param("endHour", hourLessThanEndCond.getEndHour().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());

		}

		@DisplayName("이탈 유저")
		@Test
		void searchDailyChurnUserByHour() throws Exception {

			ArrayList<DailyResponseDto.DailyChurnUser> mockData = new ArrayList<>();
			mockData.add(new DailyResponseDto.DailyChurnUser(TEST_DATE, 1L));
			Page<DailyResponseDto.DailyChurnUser> dailyChurnUsers = new PageImpl<>(mockData, page, 1);

			when(dailyService.getDailyChurnUser(any(SearchConditionDto.class), any(Pageable.class))).thenReturn(
				dailyChurnUsers);

			mock.perform(
				get("/api/daily/churn-user")
					.param("hour", hourMinCond.getHour().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());

			mock.perform(
				get("/api/daily/churn-user")
					.param("hour", hourMaxCond.getHour().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());

			mock.perform(
				get("/api/daily/churn-user")
					.param("startHour", hourRangeCond.getStartHour().toString())
					.param("endHour", hourRangeCond.getEndHour().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());

			mock.perform(
				get("/api/daily/churn-user")
					.param("startHour", hourGreaterThanStartCond.getStartHour().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());

			mock.perform(
				get("/api/daily/churn-user")
					.param("endHour", hourLessThanEndCond.getEndHour().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());
		}

		@DisplayName("매출")
		@Test
		void searchDailyPayAmountsByHour() throws Exception {

			ArrayList<DailyResponseDto.DailyPayAmount> mockData = new ArrayList<>();
			mockData.add(new DailyResponseDto.DailyPayAmount(TEST_DATE, BigDecimal.ONE));
			Page<DailyResponseDto.DailyPayAmount> dailyPayAmounts = new PageImpl<>(mockData, page, 1);

			when(dailyService.getDailyPayAmount(any(SearchConditionDto.class), any(Pageable.class))).thenReturn(
				dailyPayAmounts);

			mock.perform(
				get("/api/daily/pay-amount")
					.param("hour", hourMinCond.getHour().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());

			mock.perform(
				get("/api/daily/pay-amount")
					.param("hour", hourMaxCond.getHour().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());

			mock.perform(
				get("/api/daily/pay-amount")
					.param("startHour", hourRangeCond.getStartHour().toString())
					.param("endHour", hourRangeCond.getEndHour().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());

			mock.perform(
				get("/api/daily/pay-amount")
					.param("startHour", hourGreaterThanStartCond.getStartHour().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());

			mock.perform(
				get("/api/daily/pay-amount")
					.param("endHour", hourLessThanEndCond.getEndHour().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());
		}

		@DisplayName("비용")
		@Test
		void searchDailyCostByHour() throws Exception {

			ArrayList<DailyResponseDto.DailyCost> mockData = new ArrayList<>();
			mockData.add(new DailyResponseDto.DailyCost(TEST_DATE, BigDecimal.ONE));
			Page<DailyResponseDto.DailyCost> dailyCosts = new PageImpl<>(mockData, page, 1);

			when(dailyService.getDailyCost(any(SearchConditionDto.class), any(Pageable.class))).thenReturn(dailyCosts);

			mock.perform(
				get("/api/daily/cost")
					.param("hour", hourMinCond.getHour().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());

			mock.perform(
				get("/api/daily/cost")
					.param("hour", hourMaxCond.getHour().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());

			mock.perform(
				get("/api/daily/cost")
					.param("startHour", hourRangeCond.getStartHour().toString())
					.param("endHour", hourRangeCond.getEndHour().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());

			mock.perform(
				get("/api/daily/cost")
					.param("startHour", hourGreaterThanStartCond.getStartHour().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());

			mock.perform(
				get("/api/daily/cost")
					.param("endHour", hourLessThanEndCond.getEndHour().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());
		}

		@DisplayName("판매 금액")
		@Test
		void searchDailySalesAmountByHour() throws Exception {

			ArrayList<DailyResponseDto.DailySalesAmount> mockData = new ArrayList<>();
			mockData.add(new DailyResponseDto.DailySalesAmount(TEST_DATE, BigDecimal.ONE));
			Page<DailyResponseDto.DailySalesAmount> dailySalesAmounts = new PageImpl<>(mockData, page, 1);

			when(dailyService.getDailySalesAmount(any(SearchConditionDto.class), any(Pageable.class))).thenReturn(
				dailySalesAmounts);

			mock.perform(
				get("/api/daily/sales-amount")
					.param("hour", hourMinCond.getHour().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());

			mock.perform(
				get("/api/daily/sales-amount")
					.param("hour", hourMaxCond.getHour().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());

			mock.perform(
				get("/api/daily/sales-amount")
					.param("startHour", hourRangeCond.getStartHour().toString())
					.param("endHour", hourRangeCond.getEndHour().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());

			mock.perform(
				get("/api/daily/sales-amount")
					.param("startHour", hourGreaterThanStartCond.getStartHour().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());

			mock.perform(
				get("/api/daily/sales-amount")
					.param("endHour", hourLessThanEndCond.getEndHour().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());
		}
	}

	@Nested
	@DisplayName("시간 조건 요청 실패")
	class SearchByHourFail {

		@DisplayName("신규 유저")
		@Test
		void searchDailyNewUserByHourFail() throws Exception {

			mock.perform(
					get("/api/daily/new-user")
						.param("hour", hourUnderMinCond.getHour().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/daily/new-user")
						.param("hour", hourOverMaxCond.getHour().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/daily/new-user")
						.param("startHour", invalidHourRangeCond.getStartHour().toString())
						.param("endHour", invalidHourRangeCond.getEndHour().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/daily/new-user")
						.param("startHour", invalidHourRangeWithStartCond.getStartHour().toString())
						.param("hour", invalidHourRangeWithStartCond.getHour().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/daily/new-user")
						.param("hour", invalidHourRangeWithEndCond.getHour().toString())
						.param("endHour", invalidHourRangeWithEndCond.getEndHour().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

		}

		@DisplayName("이탈 유저")
		@Test
		void searchDailyChurnUserByHourFail() throws Exception {
			mock.perform(
					get("/api/daily/churn-user")
						.param("hour", hourUnderMinCond.getHour().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/daily/churn-user")
						.param("hour", hourOverMaxCond.getHour().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/daily/churn-user")
						.param("startHour", invalidHourRangeCond.getStartHour().toString())
						.param("endHour", invalidHourRangeCond.getEndHour().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/daily/churn-user")
						.param("startHour", invalidHourRangeWithStartCond.getStartHour().toString())
						.param("hour", invalidHourRangeWithStartCond.getHour().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/daily/churn-user")
						.param("hour", invalidHourRangeWithEndCond.getHour().toString())
						.param("endHour", invalidHourRangeWithEndCond.getEndHour().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());
		}

		@DisplayName("매출")
		@Test
		void searchDailyPayAmountsByHourFail() throws Exception {

			mock.perform(
					get("/api/daily/pay-amount")
						.param("hour", hourUnderMinCond.getHour().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/daily/pay-amount")
						.param("hour", hourOverMaxCond.getHour().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/daily/pay-amount")
						.param("startHour", invalidHourRangeCond.getStartHour().toString())
						.param("endHour", invalidHourRangeCond.getEndHour().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/daily/pay-amount")
						.param("startHour", invalidHourRangeWithStartCond.getStartHour().toString())
						.param("hour", invalidHourRangeWithStartCond.getHour().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/daily/pay-amount")
						.param("hour", invalidHourRangeWithEndCond.getHour().toString())
						.param("endHour", invalidHourRangeWithEndCond.getEndHour().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

		}

		@DisplayName("비용")
		@Test
		void searchDailyCostByHourFail() throws Exception {

			mock.perform(
					get("/api/daily/cost")
						.param("hour", hourUnderMinCond.getHour().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/daily/cost")
						.param("hour", hourOverMaxCond.getHour().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/daily/cost")
						.param("startHour", invalidHourRangeCond.getStartHour().toString())
						.param("endHour", invalidHourRangeCond.getEndHour().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/daily/cost")
						.param("startHour", invalidHourRangeWithStartCond.getStartHour().toString())
						.param("hour", invalidHourRangeWithStartCond.getHour().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/daily/cost")
						.param("hour", invalidHourRangeWithEndCond.getHour().toString())
						.param("endHour", invalidHourRangeWithEndCond.getEndHour().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

		}

		@DisplayName("판매 금액")
		@Test
		void searchDailySalesAmountByHourFail() throws Exception {
			mock.perform(
					get("/api/daily/sales-amount")
						.param("hour", hourUnderMinCond.getHour().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/daily/sales-amount")
						.param("hour", hourOverMaxCond.getHour().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/daily/sales-amount")
						.param("startHour", invalidHourRangeCond.getStartHour().toString())
						.param("endHour", invalidHourRangeCond.getEndHour().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/daily/sales-amount")
						.param("startHour", invalidHourRangeWithStartCond.getStartHour().toString())
						.param("hour", invalidHourRangeWithStartCond.getHour().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/daily/sales-amount")
						.param("hour", invalidHourRangeWithEndCond.getHour().toString())
						.param("endHour", invalidHourRangeWithEndCond.getEndHour().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());
		}
	}

	@Nested
	@DisplayName("날짜 조건 요청 성공")
	class SearchByDateSuccess {

		@DisplayName("신규 유저")
		@Test
		void searchDailyNewUserByDate() throws Exception {

			ArrayList<DailyResponseDto.DailyNewUser> mockData = new ArrayList<>();
			mockData.add(new DailyResponseDto.DailyNewUser(TEST_DATE, 1L));
			Page<DailyResponseDto.DailyNewUser> dailyNewUsers = new PageImpl<>(mockData, page, 1);

			when(dailyService.getDailyNewUser(any(SearchConditionDto.class), any(Pageable.class))).thenReturn(
				dailyNewUsers);

			mock.perform(
				get("/api/daily/new-user")
					.param("date", datePastCond.getDate().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());

			mock.perform(
				get("/api/daily/new-user")
					.param("startDate", dateRangeCond.getStartDate().toString())
					.param("endDate", dateRangeCond.getEndDate().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());

			mock.perform(
				get("/api/daily/new-user")
					.param("startDate", dateGreaterThanStartCond.getStartDate().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());

			mock.perform(
				get("/api/daily/new-user")
					.param("endDate", dateLessThanEndCond.getEndDate().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());

		}

		@DisplayName("이탈 유저")
		@Test
		void searchDailyChurnUserByDate() throws Exception {

			ArrayList<DailyResponseDto.DailyChurnUser> mockData = new ArrayList<>();
			mockData.add(new DailyResponseDto.DailyChurnUser(TEST_DATE, 1L));
			Page<DailyResponseDto.DailyChurnUser> dailyChurnUsers = new PageImpl<>(mockData, page, 1);

			when(dailyService.getDailyChurnUser(any(SearchConditionDto.class), any(Pageable.class))).thenReturn(
				dailyChurnUsers);

			mock.perform(
				get("/api/daily/churn-user")
					.param("date", datePastCond.getDate().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());

			mock.perform(
				get("/api/daily/churn-user")
					.param("startDate", dateRangeCond.getStartDate().toString())
					.param("endDate", dateRangeCond.getEndDate().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());

			mock.perform(
				get("/api/daily/churn-user")
					.param("startDate", dateGreaterThanStartCond.getStartDate().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());

			mock.perform(
				get("/api/daily/churn-user")
					.param("endDate", dateLessThanEndCond.getEndDate().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());
		}

		@DisplayName("매출")
		@Test
		void searchDailyPayAmountsByDate() throws Exception {

			ArrayList<DailyResponseDto.DailyPayAmount> mockData = new ArrayList<>();
			mockData.add(new DailyResponseDto.DailyPayAmount(TEST_DATE, BigDecimal.ONE));
			Page<DailyResponseDto.DailyPayAmount> dailyPayAmounts = new PageImpl<>(mockData, page, 1);

			when(dailyService.getDailyPayAmount(any(SearchConditionDto.class), any(Pageable.class))).thenReturn(
				dailyPayAmounts);

			mock.perform(
				get("/api/daily/pay-amount")
					.param("date", datePastCond.getDate().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());

			mock.perform(
				get("/api/daily/pay-amount")
					.param("startDate", dateRangeCond.getStartDate().toString())
					.param("endDate", dateRangeCond.getEndDate().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());

			mock.perform(
				get("/api/daily/pay-amount")
					.param("startDate", dateGreaterThanStartCond.getStartDate().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());

			mock.perform(
				get("/api/daily/pay-amount")
					.param("endDate", dateLessThanEndCond.getEndDate().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());
		}

		@DisplayName("비용")
		@Test
		void searchDailyCostByDate() throws Exception {

			ArrayList<DailyResponseDto.DailyCost> mockData = new ArrayList<>();
			mockData.add(new DailyResponseDto.DailyCost(TEST_DATE, BigDecimal.ONE));
			Page<DailyResponseDto.DailyCost> dailyCosts = new PageImpl<>(mockData, page, 1);

			when(dailyService.getDailyCost(any(SearchConditionDto.class), any(Pageable.class))).thenReturn(dailyCosts);

			mock.perform(
				get("/api/daily/cost")
					.param("date", datePastCond.getDate().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());

			mock.perform(
				get("/api/daily/cost")
					.param("startDate", dateRangeCond.getStartDate().toString())
					.param("endDate", dateRangeCond.getEndDate().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());

			mock.perform(
				get("/api/daily/cost")
					.param("startDate", dateGreaterThanStartCond.getStartDate().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());

			mock.perform(
				get("/api/daily/cost")
					.param("endDate", dateLessThanEndCond.getEndDate().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());
		}

		@DisplayName("판매 금액")
		@Test
		void searchDailySalesAmountByDate() throws Exception {

			ArrayList<DailyResponseDto.DailySalesAmount> mockData = new ArrayList<>();
			mockData.add(new DailyResponseDto.DailySalesAmount(TEST_DATE, BigDecimal.ONE));
			Page<DailyResponseDto.DailySalesAmount> dailySalesAmounts = new PageImpl<>(mockData, page, 1);

			when(dailyService.getDailySalesAmount(any(SearchConditionDto.class), any(Pageable.class))).thenReturn(
				dailySalesAmounts);

			mock.perform(
				get("/api/daily/sales-amount")
					.param("date", datePastCond.getDate().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());

			mock.perform(
				get("/api/daily/sales-amount")
					.param("startDate", dateRangeCond.getStartDate().toString())
					.param("endDate", dateRangeCond.getEndDate().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());

			mock.perform(
				get("/api/daily/sales-amount")
					.param("startDate", dateGreaterThanStartCond.getStartDate().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());

			mock.perform(
				get("/api/daily/sales-amount")
					.param("endDate", dateLessThanEndCond.getEndDate().toString())
					.param("page", page.toString())
					.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk());
		}
	}

	@Nested
	@DisplayName("날짜 조건 요청 실패")
	class SearchByDateFail {

		@DisplayName("신규 유저")
		@Test
		void searchDailyNewUserByDateFail() throws Exception {

			mock.perform(
					get("/api/daily/new-user")
						.param("date", datePresentCond.getDate().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/daily/new-user")
						.param("date", dateFutureCond.getDate().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/daily/new-user")
						.param("startDate", invalidDateRangeCond.getStartDate().toString())
						.param("endDate", invalidDateRangeCond.getEndDate().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/daily/new-user")
						.param("startDate", invalidDateRangeWithStartCond.getStartDate().toString())
						.param("date", invalidDateRangeWithStartCond.getDate().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/daily/new-user")
						.param("date", invalidDateRangeWithEndCond.getDate().toString())
						.param("endDate", invalidDateRangeWithEndCond.getEndDate().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());
		}

		@DisplayName("이탈 유저")
		@Test
		void searchDailyChurnUserByDateFail() throws Exception {

			mock.perform(
					get("/api/daily/churn-user")
						.param("date", datePresentCond.getDate().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/daily/churn-user")
						.param("date", dateFutureCond.getDate().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/daily/churn-user")
						.param("startDate", invalidDateRangeCond.getStartDate().toString())
						.param("endDate", invalidDateRangeCond.getEndDate().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/daily/churn-user")
						.param("startDate", invalidDateRangeWithStartCond.getStartDate().toString())
						.param("date", invalidDateRangeWithStartCond.getDate().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/daily/churn-user")
						.param("date", invalidDateRangeWithEndCond.getDate().toString())
						.param("endDate", invalidDateRangeWithEndCond.getEndDate().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

		}

		@DisplayName("매출")
		@Test
		void searchDailyPayAmountsByDateFail() throws Exception {

			mock.perform(
					get("/api/daily/pay-amount")
						.param("date", datePresentCond.getDate().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/daily/pay-amount")
						.param("date", dateFutureCond.getDate().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/daily/pay-amount")
						.param("startDate", invalidDateRangeCond.getStartDate().toString())
						.param("endDate", invalidDateRangeCond.getEndDate().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/daily/pay-amount")
						.param("startDate", invalidDateRangeWithStartCond.getStartDate().toString())
						.param("date", invalidDateRangeWithStartCond.getDate().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/daily/pay-amount")
						.param("date", invalidDateRangeWithEndCond.getDate().toString())
						.param("endDate", invalidDateRangeWithEndCond.getEndDate().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());
		}

		@DisplayName("비용")
		@Test
		void searchDailyCostByDateFail() throws Exception {

			mock.perform(
					get("/api/daily/cost")
						.param("date", datePresentCond.getDate().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/daily/cost")
						.param("date", dateFutureCond.getDate().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/daily/cost")
						.param("startDate", invalidDateRangeCond.getStartDate().toString())
						.param("endDate", invalidDateRangeCond.getEndDate().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/daily/cost")
						.param("startDate", invalidDateRangeWithStartCond.getStartDate().toString())
						.param("date", invalidDateRangeWithStartCond.getDate().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/daily/cost")
						.param("date", invalidDateRangeWithEndCond.getDate().toString())
						.param("endDate", invalidDateRangeWithEndCond.getEndDate().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());
		}

		@DisplayName("판매 금액")
		@Test
		void searchDailySalesAmountByDateFail() throws Exception {

			mock.perform(
					get("/api/daily/sales-amount")
						.param("date", datePresentCond.getDate().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/daily/sales-amount")
						.param("date", dateFutureCond.getDate().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/daily/sales-amount")
						.param("startDate", invalidDateRangeCond.getStartDate().toString())
						.param("endDate", invalidDateRangeCond.getEndDate().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/daily/sales-amount")
						.param("startDate", invalidDateRangeWithStartCond.getStartDate().toString())
						.param("date", invalidDateRangeWithStartCond.getDate().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

			mock.perform(
					get("/api/daily/sales-amount")
						.param("date", invalidDateRangeWithEndCond.getDate().toString())
						.param("endDate", invalidDateRangeWithEndCond.getEndDate().toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());
		}
	}

}