package com.daou.api.controller.command;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindException;

import com.daou.api.dto.request.DailySummaryRequestDto;
import com.daou.api.dto.request.HourlySummaryRequestDto;
import com.daou.api.service.SummaryCommandService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@ExtendWith(MockitoExtension.class)
class HourlySummaryCommandControllerTest {

	@InjectMocks
	HourlySummaryCommandController summaryController;

	@Mock
	SummaryCommandService summaryService;

	ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

	private MockMvc mock;

	@BeforeEach
	public void init() {
		mock = MockMvcBuilders.standaloneSetup(summaryController)
			.build();
	}

	@DisplayName("저장")
	@Nested
	class Save {

		@DisplayName("시간별 누적 집계 성공")
		@Test
		void saveHourlySummarySuccess() throws Exception {

			HourlySummaryRequestDto.SaveRequest saveData = HourlySummaryRequestDto.SaveRequest.builder()
				.hour(0)
				.sumNewUser(10L)
				.sumChurnUser(10L)
				.sumPayAmount(BigDecimal.valueOf(100L))
				.sumCost(BigDecimal.valueOf(100L))
				.sumSalesAmount(BigDecimal.valueOf(100L))
				.avgNewUser(10D)
				.avgChurnUser(10D)
				.avgPayAmount(10D)
				.avgCost(10D)
				.avgSalesAmount(10D)
				.maxNewUser(10L)
				.maxChurnUser(10L)
				.maxPayAmount(BigDecimal.valueOf(100L))
				.maxCost(BigDecimal.valueOf(100L))
				.maxSalesAmount(BigDecimal.valueOf(100L))
				.minNewUser(10L)
				.minChurnUser(10L)
				.minPayAmount(BigDecimal.valueOf(100L))
				.minCost(BigDecimal.valueOf(100L))
				.minSalesAmount(BigDecimal.valueOf(100L))
				.build();

			String saveStr = objectMapper.writeValueAsString(saveData);
			byte[] saveBytes = objectMapper.writeValueAsBytes(saveData);

			mock.perform(
					MockMvcRequestBuilders.post("/api/hourly-summary")
						.contentType(MediaType.APPLICATION_JSON)
						.content(saveStr)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(status().isOk());

			mock.perform(
					MockMvcRequestBuilders.post("/api/hourly-summary")
						.contentType(MediaType.APPLICATION_JSON)
						.content(saveBytes)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(status().isOk());

		}

		@DisplayName("날짜별 누적 집계 성공")
		@Test
		void saveDailySummarySuccess() throws Exception {

			DailySummaryRequestDto.SaveRequest saveData = DailySummaryRequestDto.SaveRequest.builder()
				.date(LocalDate.parse("2022-12-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")))
				.sumNewUser(10L)
				.sumChurnUser(10L)
				.sumPayAmount(BigDecimal.valueOf(100L))
				.sumCost(BigDecimal.valueOf(100L))
				.sumSalesAmount(BigDecimal.valueOf(100L))
				.avgNewUser(10D)
				.avgChurnUser(10D)
				.avgPayAmount(10D)
				.avgCost(10D)
				.avgSalesAmount(10D)
				.maxNewUser(10L)
				.maxChurnUser(10L)
				.maxPayAmount(BigDecimal.valueOf(100L))
				.maxCost(BigDecimal.valueOf(100L))
				.maxSalesAmount(BigDecimal.valueOf(100L))
				.minNewUser(10L)
				.minChurnUser(10L)
				.minPayAmount(BigDecimal.valueOf(100L))
				.minCost(BigDecimal.valueOf(100L))
				.minSalesAmount(BigDecimal.valueOf(100L))
				.build();

			String saveStr = objectMapper.writeValueAsString(saveData);
			byte[] saveBytes = objectMapper.writeValueAsBytes(saveData);

			mock.perform(
					MockMvcRequestBuilders.post("/api/daily-summary")
						.contentType(MediaType.APPLICATION_JSON)
						.content(saveStr)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(status().isOk());

			mock.perform(
					MockMvcRequestBuilders.post("/api/daily-summary")
						.contentType(MediaType.APPLICATION_JSON)
						.content(saveBytes)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(status().isOk());
		}

		@DisplayName("필수값 누락 - 시간")
		@Test
		void noHour() throws Exception {

			HourlySummaryRequestDto.SaveRequest saveData = HourlySummaryRequestDto.SaveRequest.builder()
				.sumNewUser(10L)
				.sumChurnUser(10L)
				.sumPayAmount(BigDecimal.valueOf(100L))
				.sumCost(BigDecimal.valueOf(100L))
				.sumSalesAmount(BigDecimal.valueOf(100L))
				.avgNewUser(10D)
				.avgChurnUser(10D)
				.avgPayAmount(10D)
				.avgCost(10D)
				.avgSalesAmount(10D)
				.maxNewUser(10L)
				.maxChurnUser(10L)
				.maxPayAmount(BigDecimal.valueOf(100L))
				.maxCost(BigDecimal.valueOf(100L))
				.maxSalesAmount(BigDecimal.valueOf(100L))
				.minNewUser(10L)
				.minChurnUser(10L)
				.minPayAmount(BigDecimal.valueOf(100L))
				.minCost(BigDecimal.valueOf(100L))
				.minSalesAmount(BigDecimal.valueOf(100L))
				.build();

			String saveStr = objectMapper.writeValueAsString(saveData);

			mock.perform(
					MockMvcRequestBuilders.post("/api/hourly-summary")
						.contentType(MediaType.APPLICATION_JSON)
						.content(saveStr)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

		}

		@DisplayName("필수값 누락 - 날짜")
		@Test
		void noDate() throws Exception {

			DailySummaryRequestDto.SaveRequest saveData = DailySummaryRequestDto.SaveRequest.builder()
				.sumNewUser(10L)
				.sumChurnUser(10L)
				.sumPayAmount(BigDecimal.valueOf(100L))
				.sumCost(BigDecimal.valueOf(100L))
				.sumSalesAmount(BigDecimal.valueOf(100L))
				.avgNewUser(10D)
				.avgChurnUser(10D)
				.avgPayAmount(10D)
				.avgCost(10D)
				.avgSalesAmount(10D)
				.maxNewUser(10L)
				.maxChurnUser(10L)
				.maxPayAmount(BigDecimal.valueOf(100L))
				.maxCost(BigDecimal.valueOf(100L))
				.maxSalesAmount(BigDecimal.valueOf(100L))
				.minNewUser(10L)
				.minChurnUser(10L)
				.minPayAmount(BigDecimal.valueOf(100L))
				.minCost(BigDecimal.valueOf(100L))
				.minSalesAmount(BigDecimal.valueOf(100L))
				.build();

			String saveStr = objectMapper.writeValueAsString(saveData);

			mock.perform(
					MockMvcRequestBuilders.post("/api/daily-summary")
						.contentType(MediaType.APPLICATION_JSON)
						.content(saveStr)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());
		}

		@DisplayName("유효성 검증 실패 - 시간")
		@Test
		void invalidHourSaveFail() throws Exception {

			HourlySummaryRequestDto.SaveRequest underMinHour = HourlySummaryRequestDto.SaveRequest.builder()
				.hour(-1)
				.build();

			HourlySummaryRequestDto.SaveRequest overMaxHour = HourlySummaryRequestDto.SaveRequest.builder()
				.hour(24)
				.build();

			String underMinHourStr = objectMapper.writeValueAsString(underMinHour);
			String overMaxHourStr = objectMapper.writeValueAsString(overMaxHour);

			mock.perform(
					MockMvcRequestBuilders.post("/api/hourly-summary")
						.contentType(MediaType.APPLICATION_JSON)
						.content(underMinHourStr)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

			mock.perform(
					MockMvcRequestBuilders.post("/api/hourly-summary")
						.contentType(MediaType.APPLICATION_JSON)
						.content(overMaxHourStr)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());
		}

		@DisplayName("유효성 검증 실패 - 날짜")
		@Test
		void invalidDateSaveFail() throws Exception {

			DailySummaryRequestDto.SaveRequest futureData = DailySummaryRequestDto.SaveRequest.builder()
				.date(LocalDate.parse(LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
				.build();

			String futureStr = objectMapper.writeValueAsString(futureData);

			mock.perform(
					MockMvcRequestBuilders.post("/api/daily-summary")
						.contentType(MediaType.APPLICATION_JSON)
						.content(futureStr)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

		}

		@DisplayName("유효성 검증 실패 - sum ")
		@Test
		void invalidSumSaveFail() throws Exception {

			HourlySummaryRequestDto.SaveRequest minusSumHourlySummary = HourlySummaryRequestDto.SaveRequest.builder()
				.hour(0)
				.sumNewUser(-1L)
				.build();

			DailySummaryRequestDto.SaveRequest minusSumDailySummary = DailySummaryRequestDto.SaveRequest.builder()
				.date(LocalDate.parse("2022-12-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")))
				.sumNewUser(-1L)
				.build();

			String hourlySummaryStr = objectMapper.writeValueAsString(minusSumHourlySummary);
			String dailySummaryStr = objectMapper.writeValueAsString(minusSumDailySummary);

			mock.perform(
					MockMvcRequestBuilders.post("/api/hourly-summary")
						.contentType(MediaType.APPLICATION_JSON)
						.content(hourlySummaryStr)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

			mock.perform(
					MockMvcRequestBuilders.post("/api/daily-summary")
						.contentType(MediaType.APPLICATION_JSON)
						.content(dailySummaryStr)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());
		}

		@DisplayName("유효성 검증 실패 - avg ")
		@Test
		void invalidAvgSaveFail() throws Exception {

			HourlySummaryRequestDto.SaveRequest minusAvgHourlySummary = HourlySummaryRequestDto.SaveRequest.builder()
				.hour(0)
				.avgNewUser(-1D)
				.build();

			DailySummaryRequestDto.SaveRequest minusAvgDailySummary = DailySummaryRequestDto.SaveRequest.builder()
				.date(LocalDate.parse("2022-12-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")))
				.avgNewUser(-1D)
				.build();

			String hourlySummaryStr = objectMapper.writeValueAsString(minusAvgHourlySummary);
			String dailySummaryStr = objectMapper.writeValueAsString(minusAvgDailySummary);

			mock.perform(
					MockMvcRequestBuilders.post("/api/hourly-summary")
						.contentType(MediaType.APPLICATION_JSON)
						.content(hourlySummaryStr)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

			mock.perform(
					MockMvcRequestBuilders.post("/api/daily-summary")
						.contentType(MediaType.APPLICATION_JSON)
						.content(dailySummaryStr)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());
		}

		@DisplayName("유효성 검증 실패 - max ")
		@Test
		void invalidMaxSaveFail() throws Exception {

			HourlySummaryRequestDto.SaveRequest minusMaxHourlySummary = HourlySummaryRequestDto.SaveRequest.builder()
				.hour(0)
				.maxNewUser(-1L)
				.build();

			DailySummaryRequestDto.SaveRequest minusMaxDailySummary = DailySummaryRequestDto.SaveRequest.builder()
				.date(LocalDate.parse("2022-12-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")))
				.maxNewUser(-1L)
				.build();

			String hourlySummaryStr = objectMapper.writeValueAsString(minusMaxHourlySummary);
			String dailySummaryStr = objectMapper.writeValueAsString(minusMaxDailySummary);

			mock.perform(
					MockMvcRequestBuilders.post("/api/hourly-summary")
						.contentType(MediaType.APPLICATION_JSON)
						.content(hourlySummaryStr)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

			mock.perform(
					MockMvcRequestBuilders.post("/api/daily-summary")
						.contentType(MediaType.APPLICATION_JSON)
						.content(dailySummaryStr)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());
		}

		@DisplayName("유효성 검증 실패 - min ")
		@Test
		void invalidMinSaveFail() throws Exception {

			HourlySummaryRequestDto.SaveRequest minusMinHourlySummary = HourlySummaryRequestDto.SaveRequest.builder()
				.hour(0)
				.minNewUser(-1L)
				.build();

			DailySummaryRequestDto.SaveRequest minusMinDailySummary = DailySummaryRequestDto.SaveRequest.builder()
				.date(LocalDate.parse("2022-12-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")))
				.minNewUser(-1L)
				.build();

			String hourlySummaryStr = objectMapper.writeValueAsString(minusMinHourlySummary);
			String dailySummaryStr = objectMapper.writeValueAsString(minusMinDailySummary);

			mock.perform(
					MockMvcRequestBuilders.post("/api/hourly-summary")
						.contentType(MediaType.APPLICATION_JSON)
						.content(hourlySummaryStr)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

			mock.perform(
					MockMvcRequestBuilders.post("/api/daily-summary")
						.contentType(MediaType.APPLICATION_JSON)
						.content(dailySummaryStr)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());
		}

	}

	@DisplayName("수정")
	@Nested
	class Update {

		@DisplayName("시간별 누적 집계 성공")
		@Test
		void updateHourlySummarySuccess() throws Exception {

			HourlySummaryRequestDto.UpdateRequest updateData = HourlySummaryRequestDto.UpdateRequest.builder()
				.hour(0)
				.sumNewUser(10L)
				.sumChurnUser(10L)
				.sumPayAmount(BigDecimal.valueOf(100L))
				.sumCost(BigDecimal.valueOf(100L))
				.sumSalesAmount(BigDecimal.valueOf(100L))
				.avgNewUser(10D)
				.avgChurnUser(10D)
				.avgPayAmount(10D)
				.avgCost(10D)
				.avgSalesAmount(10D)
				.maxNewUser(10L)
				.maxChurnUser(10L)
				.maxPayAmount(BigDecimal.valueOf(100L))
				.maxCost(BigDecimal.valueOf(100L))
				.maxSalesAmount(BigDecimal.valueOf(100L))
				.minNewUser(10L)
				.minChurnUser(10L)
				.minPayAmount(BigDecimal.valueOf(100L))
				.minCost(BigDecimal.valueOf(100L))
				.minSalesAmount(BigDecimal.valueOf(100L))
				.build();

			String updateStr = objectMapper.writeValueAsString(updateData);
			byte[] updateBytes = objectMapper.writeValueAsBytes(updateData);

			mock.perform(
					MockMvcRequestBuilders.put("/api/hourly-summary")
						.contentType(MediaType.APPLICATION_JSON)
						.content(updateStr)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(status().isOk());

			mock.perform(
					MockMvcRequestBuilders.put("/api/hourly-summary")
						.contentType(MediaType.APPLICATION_JSON)
						.content(updateBytes)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(status().isOk());

		}

		@DisplayName("날짜별 누적 집계 성공")
		@Test
		void updateDailySummarySuccess() throws Exception {

			DailySummaryRequestDto.UpdateRequest updateData = DailySummaryRequestDto.UpdateRequest.builder()
				.date(LocalDate.parse("2022-12-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")))
				.sumNewUser(10L)
				.sumChurnUser(10L)
				.sumPayAmount(BigDecimal.valueOf(100L))
				.sumCost(BigDecimal.valueOf(100L))
				.sumSalesAmount(BigDecimal.valueOf(100L))
				.avgNewUser(10D)
				.avgChurnUser(10D)
				.avgPayAmount(10D)
				.avgCost(10D)
				.avgSalesAmount(10D)
				.maxNewUser(10L)
				.maxChurnUser(10L)
				.maxPayAmount(BigDecimal.valueOf(100L))
				.maxCost(BigDecimal.valueOf(100L))
				.maxSalesAmount(BigDecimal.valueOf(100L))
				.minNewUser(10L)
				.minChurnUser(10L)
				.minPayAmount(BigDecimal.valueOf(100L))
				.minCost(BigDecimal.valueOf(100L))
				.minSalesAmount(BigDecimal.valueOf(100L))
				.build();

			String updateStr = objectMapper.writeValueAsString(updateData);
			byte[] updateBytes = objectMapper.writeValueAsBytes(updateData);

			mock.perform(
					MockMvcRequestBuilders.put("/api/daily-summary")
						.contentType(MediaType.APPLICATION_JSON)
						.content(updateStr)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(status().isOk());

			mock.perform(
					MockMvcRequestBuilders.put("/api/daily-summary")
						.contentType(MediaType.APPLICATION_JSON)
						.content(updateBytes)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(status().isOk());
		}

		@DisplayName("필수값 누락 - 시간")
		@Test
		void noHour() throws Exception {

			HourlySummaryRequestDto.UpdateRequest updateData = HourlySummaryRequestDto.UpdateRequest.builder()
				.sumNewUser(10L)
				.sumChurnUser(10L)
				.sumPayAmount(BigDecimal.valueOf(100L))
				.sumCost(BigDecimal.valueOf(100L))
				.sumSalesAmount(BigDecimal.valueOf(100L))
				.avgNewUser(10D)
				.avgChurnUser(10D)
				.avgPayAmount(10D)
				.avgCost(10D)
				.avgSalesAmount(10D)
				.maxNewUser(10L)
				.maxChurnUser(10L)
				.maxPayAmount(BigDecimal.valueOf(100L))
				.maxCost(BigDecimal.valueOf(100L))
				.maxSalesAmount(BigDecimal.valueOf(100L))
				.minNewUser(10L)
				.minChurnUser(10L)
				.minPayAmount(BigDecimal.valueOf(100L))
				.minCost(BigDecimal.valueOf(100L))
				.minSalesAmount(BigDecimal.valueOf(100L))
				.build();

			String updateStr = objectMapper.writeValueAsString(updateData);

			mock.perform(
					MockMvcRequestBuilders.put("/api/hourly-summary")
						.contentType(MediaType.APPLICATION_JSON)
						.content(updateStr)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

		}

		@DisplayName("필수값 누락 - 날짜")
		@Test
		void noDate() throws Exception {

			DailySummaryRequestDto.UpdateRequest updateData = DailySummaryRequestDto.UpdateRequest.builder()
				.sumNewUser(10L)
				.sumChurnUser(10L)
				.sumPayAmount(BigDecimal.valueOf(100L))
				.sumCost(BigDecimal.valueOf(100L))
				.sumSalesAmount(BigDecimal.valueOf(100L))
				.avgNewUser(10D)
				.avgChurnUser(10D)
				.avgPayAmount(10D)
				.avgCost(10D)
				.avgSalesAmount(10D)
				.maxNewUser(10L)
				.maxChurnUser(10L)
				.maxPayAmount(BigDecimal.valueOf(100L))
				.maxCost(BigDecimal.valueOf(100L))
				.maxSalesAmount(BigDecimal.valueOf(100L))
				.minNewUser(10L)
				.minChurnUser(10L)
				.minPayAmount(BigDecimal.valueOf(100L))
				.minCost(BigDecimal.valueOf(100L))
				.minSalesAmount(BigDecimal.valueOf(100L))
				.build();

			String updateStr = objectMapper.writeValueAsString(updateData);

			mock.perform(
					MockMvcRequestBuilders.put("/api/daily-summary")
						.contentType(MediaType.APPLICATION_JSON)
						.content(updateStr)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());
		}

		@DisplayName("유효성 검증 실패 - 시간")
		@Test
		void invalidHourUpdateFail() throws Exception {

			HourlySummaryRequestDto.UpdateRequest underMinHour = HourlySummaryRequestDto.UpdateRequest.builder()
				.hour(-1)
				.build();

			HourlySummaryRequestDto.UpdateRequest overMaxHour = HourlySummaryRequestDto.UpdateRequest.builder()
				.hour(24)
				.build();

			String underMinHourStr = objectMapper.writeValueAsString(underMinHour);
			String overMaxHourStr = objectMapper.writeValueAsString(overMaxHour);

			mock.perform(
					MockMvcRequestBuilders.put("/api/hourly-summary")
						.contentType(MediaType.APPLICATION_JSON)
						.content(underMinHourStr)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

			mock.perform(
					MockMvcRequestBuilders.put("/api/hourly-summary")
						.contentType(MediaType.APPLICATION_JSON)
						.content(overMaxHourStr)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());
		}

		@DisplayName("유효성 검증 실패 - 날짜")
		@Test
		void invalidDateUpdateFail() throws Exception {

			DailySummaryRequestDto.UpdateRequest futureData = DailySummaryRequestDto.UpdateRequest.builder()
				.date(LocalDate.parse(LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
				.build();

			String futureStr = objectMapper.writeValueAsString(futureData);

			mock.perform(
					MockMvcRequestBuilders.put("/api/daily-summary")
						.contentType(MediaType.APPLICATION_JSON)
						.content(futureStr)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());
		}

		@DisplayName("유효성 검증 실패 - sum ")
		@Test
		void invalidSumUpdateFail() throws Exception {

			HourlySummaryRequestDto.UpdateRequest minusSumHourlySummary = HourlySummaryRequestDto.UpdateRequest.builder()
				.hour(0)
				.sumNewUser(-1L)
				.build();

			DailySummaryRequestDto.UpdateRequest minusSumDailySummary = DailySummaryRequestDto.UpdateRequest.builder()
				.date(LocalDate.parse("2022-12-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")))
				.sumNewUser(-1L)
				.build();

			String hourlySummaryStr = objectMapper.writeValueAsString(minusSumHourlySummary);
			String dailySummaryStr = objectMapper.writeValueAsString(minusSumDailySummary);

			mock.perform(
					MockMvcRequestBuilders.put("/api/hourly-summary")
						.contentType(MediaType.APPLICATION_JSON)
						.content(hourlySummaryStr)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

			mock.perform(
					MockMvcRequestBuilders.put("/api/daily-summary")
						.contentType(MediaType.APPLICATION_JSON)
						.content(dailySummaryStr)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());
		}

		@DisplayName("유효성 검증 실패 - avg ")
		@Test
		void invalidAvgSaveFail() throws Exception {

			HourlySummaryRequestDto.UpdateRequest minusAvgHourlySummary = HourlySummaryRequestDto.UpdateRequest.builder()
				.hour(0)
				.avgNewUser(-1D)
				.build();

			DailySummaryRequestDto.UpdateRequest minusAvgDailySummary = DailySummaryRequestDto.UpdateRequest.builder()
				.date(LocalDate.parse("2022-12-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")))
				.avgNewUser(-1D)
				.build();

			String hourlySummaryStr = objectMapper.writeValueAsString(minusAvgHourlySummary);
			String dailySummaryStr = objectMapper.writeValueAsString(minusAvgDailySummary);

			mock.perform(
					MockMvcRequestBuilders.put("/api/hourly-summary")
						.contentType(MediaType.APPLICATION_JSON)
						.content(hourlySummaryStr)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

			mock.perform(
					MockMvcRequestBuilders.put("/api/daily-summary")
						.contentType(MediaType.APPLICATION_JSON)
						.content(dailySummaryStr)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());
		}

		@DisplayName("유효성 검증 실패 - max ")
		@Test
		void invalidMaxSaveFail() throws Exception {

			HourlySummaryRequestDto.UpdateRequest minusMaxHourlySummary = HourlySummaryRequestDto.UpdateRequest.builder()
				.hour(0)
				.maxNewUser(-1L)
				.build();

			DailySummaryRequestDto.UpdateRequest minusMaxDailySummary = DailySummaryRequestDto.UpdateRequest.builder()
				.date(LocalDate.parse("2022-12-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")))
				.maxNewUser(-1L)
				.build();

			String hourlySummaryStr = objectMapper.writeValueAsString(minusMaxHourlySummary);
			String dailySummaryStr = objectMapper.writeValueAsString(minusMaxDailySummary);

			mock.perform(
					MockMvcRequestBuilders.put("/api/hourly-summary")
						.contentType(MediaType.APPLICATION_JSON)
						.content(hourlySummaryStr)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

			mock.perform(
					MockMvcRequestBuilders.put("/api/daily-summary")
						.contentType(MediaType.APPLICATION_JSON)
						.content(dailySummaryStr)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());
		}

		@DisplayName("유효성 검증 실패 - min ")
		@Test
		void invalidMinSaveFail() throws Exception {

			HourlySummaryRequestDto.UpdateRequest minusMinHourlySummary = HourlySummaryRequestDto.UpdateRequest.builder()
				.hour(0)
				.minNewUser(-1L)
				.build();

			DailySummaryRequestDto.UpdateRequest minusMinDailySummary = DailySummaryRequestDto.UpdateRequest.builder()
				.date(LocalDate.parse("2022-12-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")))
				.minNewUser(-1L)
				.build();

			String hourlySummaryStr = objectMapper.writeValueAsString(minusMinHourlySummary);
			String dailySummaryStr = objectMapper.writeValueAsString(minusMinDailySummary);

			mock.perform(
					MockMvcRequestBuilders.put("/api/hourly-summary")
						.contentType(MediaType.APPLICATION_JSON)
						.content(hourlySummaryStr)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

			mock.perform(
					MockMvcRequestBuilders.put("/api/daily-summary")
						.contentType(MediaType.APPLICATION_JSON)
						.content(dailySummaryStr)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());
		}
	}

	@Nested
	@DisplayName("삭제")
	class Delete {

		@Test
		@DisplayName("시간별 누적 집계 성공")
		void deleteHourlySummarySuccess() throws Exception {

			mock.perform(
					MockMvcRequestBuilders.delete("/api/hourly-summary")
						.contentType(MediaType.APPLICATION_JSON)
						.param("hour", "0")
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());
		}

		@Test
		@DisplayName("날짜별 누적 집계 성공")
		void deleteDailySummarySuccess() throws Exception {

			mock.perform(
					MockMvcRequestBuilders.delete("/api/daily-summary")
						.contentType(MediaType.APPLICATION_JSON)
						.param("date", LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());
		}

		@DisplayName("유효성 검증 실패 - 날짜")
		@Test
		void invalidDateDeleteFail() throws Exception {

			mock.perform(
					MockMvcRequestBuilders.delete("/api/daily-summary")
						.contentType(MediaType.APPLICATION_JSON)
						.param("date", LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());
		}

		@DisplayName("유효성 검증 실패 - 시간")
		@Test
		void invalidHourDeleteFail() throws Exception {

			mock.perform(
					MockMvcRequestBuilders.delete("/api/hourly-summary")
						.contentType(MediaType.APPLICATION_JSON)
						.param("hour", "-1")
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

			mock.perform(
					MockMvcRequestBuilders.delete("/api/hourly-summary")
						.contentType(MediaType.APPLICATION_JSON)
						.param("hour", "24")
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());
		}
	}
}