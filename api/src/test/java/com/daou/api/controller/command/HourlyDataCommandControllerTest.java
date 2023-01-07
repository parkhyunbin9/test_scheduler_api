package com.daou.api.controller.command;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindException;

import com.daou.api.dto.request.HourlyInfoRequestDto;
import com.daou.api.service.HourlyCommandService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@ExtendWith(MockitoExtension.class)
class HourlyDataCommandControllerTest {

	@InjectMocks
	HourlyDataCommandController hourlyController;

	@Mock
	HourlyCommandService hourlyService;

	ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

	private MockMvc mock;

	@BeforeEach
	public void init() {
		mock = MockMvcBuilders.standaloneSetup(hourlyController)
			.build();
	}

	@DisplayName("저장")
	@Nested
	class Save {

		@DisplayName("성공")
		@Test
		void saveSuccess() throws Exception {

			HourlyInfoRequestDto.SaveRequest saveData = HourlyInfoRequestDto.SaveRequest.builder()
				.date(LocalDate.parse("2022-12-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")))
				.hour(0)
				.newUser(10L)
				.churnUser(12L)
				.payAmount(BigDecimal.valueOf(10L))
				.cost(BigDecimal.valueOf(10L))
				.salesAmount(BigDecimal.valueOf(10L))
				.build();

			String saveStr = objectMapper.writeValueAsString(saveData);
			byte[] saveBytes = objectMapper.writeValueAsBytes(saveData);

			mock.perform(
					post("/api/hourly-data")
						.contentType(MediaType.APPLICATION_JSON)
						.content(saveStr)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(status().isOk());

			mock.perform(
					post("/api/hourly-data")
						.contentType(MediaType.APPLICATION_JSON)
						.content(saveBytes)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(status().isOk());

		}

		@DisplayName("필수값 누락 - 날짜")
		@Test
		void withoutDateSaveFail() throws Exception {

			HourlyInfoRequestDto.SaveRequest saveData = HourlyInfoRequestDto.SaveRequest.builder()
				.hour(0)
				.newUser(10L)
				.churnUser(12L)
				.payAmount(BigDecimal.valueOf(10L))
				.cost(BigDecimal.valueOf(10L))
				.salesAmount(BigDecimal.valueOf(10L))
				.build();

			String saveStr = objectMapper.writeValueAsString(saveData);

			mock.perform(
					post("/api/hourly-data")
						.contentType(MediaType.APPLICATION_JSON)
						.content(saveStr)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

		}

		@DisplayName("필수값 누락 - 시간")
		@Test
		void withoutHourSaveFail() throws Exception {
			HourlyInfoRequestDto.SaveRequest saveData = HourlyInfoRequestDto.SaveRequest.builder()
				.date(LocalDate.parse("2022-12-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")))
				.newUser(10L)
				.churnUser(12L)
				.payAmount(BigDecimal.valueOf(10L))
				.cost(BigDecimal.valueOf(10L))
				.salesAmount(BigDecimal.valueOf(10L))
				.build();

			String saveStr = objectMapper.writeValueAsString(saveData);

			mock.perform(
					post("/api/hourly-data")
						.contentType(MediaType.APPLICATION_JSON)
						.content(saveStr)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

		}

		@DisplayName("유효성 검증 실패 - 날짜")
		@Test
		void invalidDateSaveFail() throws Exception {
			HourlyInfoRequestDto.SaveRequest futureData = HourlyInfoRequestDto.SaveRequest.builder()
				.date(LocalDate.parse(LocalDate.now().plusDays(1).format( DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
				.hour(0)
				.newUser(10L)
				.churnUser(12L)
				.payAmount(BigDecimal.valueOf(10L))
				.cost(BigDecimal.valueOf(10L))
				.salesAmount(BigDecimal.valueOf(10L))
				.build();

			String saveStr = objectMapper.writeValueAsString(futureData);

			mock.perform(
					post("/api/hourly-data")
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

			HourlyInfoRequestDto.SaveRequest underZeroHour= HourlyInfoRequestDto.SaveRequest.builder()
				.date(LocalDate.parse("2022-12-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")))
				.hour(-1)
				.newUser(10L)
				.churnUser(12L)
				.payAmount(BigDecimal.valueOf(10L))
				.cost(BigDecimal.valueOf(10L))
				.salesAmount(BigDecimal.valueOf(10L))
				.build();

			HourlyInfoRequestDto.SaveRequest overMaxHour = HourlyInfoRequestDto.SaveRequest.builder()
				.date(LocalDate.parse("2022-12-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")))
				.hour(24)
				.newUser(10L)
				.churnUser(12L)
				.payAmount(BigDecimal.valueOf(10L))
				.cost(BigDecimal.valueOf(10L))
				.salesAmount(BigDecimal.valueOf(10L))
				.build();

			String underZeroData= objectMapper.writeValueAsString(underZeroHour);
			String overMaxData= objectMapper.writeValueAsString(overMaxHour);

			mock.perform(
					post("/api/hourly-data")
						.contentType(MediaType.APPLICATION_JSON)
						.content(underZeroData)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

			mock.perform(
					post("/api/hourly-data")
						.contentType(MediaType.APPLICATION_JSON)
						.content(overMaxData)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());
		}

		@DisplayName("유효성 검증 실패 - 신규 유저")
		@Test
		void invalidNewUserSaveFail() throws Exception {

			HourlyInfoRequestDto.SaveRequest zeroNewUser = HourlyInfoRequestDto.SaveRequest.builder()
				.date(LocalDate.parse("2022-12-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")))
				.hour(0)
				.newUser(0L)
				.churnUser(12L)
				.payAmount(BigDecimal.valueOf(10L))
				.cost(BigDecimal.valueOf(10L))
				.salesAmount(BigDecimal.valueOf(10L))
				.build();

			HourlyInfoRequestDto.SaveRequest minusNewUser = HourlyInfoRequestDto.SaveRequest.builder()
				.date(LocalDate.parse(LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
				.hour(24)
				.newUser(-1L)
				.churnUser(12L)
				.payAmount(BigDecimal.valueOf(10L))
				.cost(BigDecimal.valueOf(10L))
				.salesAmount(BigDecimal.valueOf(10L))
				.build();

			String zeroNewUserData = objectMapper.writeValueAsString(zeroNewUser);
			String minusNewUserData = objectMapper.writeValueAsString(minusNewUser);

			mock.perform(
					post("/api/hourly-data")
						.contentType(MediaType.APPLICATION_JSON)
						.content(zeroNewUserData)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(status().isOk());

			mock.perform(
					post("/api/hourly-data")
						.contentType(MediaType.APPLICATION_JSON)
						.content(minusNewUserData)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());
		}

		@DisplayName("유효성 검증 실패 - 이탈 유저")
		@Test
		void invalidChurnUserSaveFail() throws Exception {

			HourlyInfoRequestDto.SaveRequest zeroChurnUser = HourlyInfoRequestDto.SaveRequest.builder()
				.date(LocalDate.parse("2022-12-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")))
				.hour(0)
				.newUser(1L)
				.churnUser(0L)
				.payAmount(BigDecimal.valueOf(10L))
				.cost(BigDecimal.valueOf(10L))
				.salesAmount(BigDecimal.valueOf(10L))
				.build();

			HourlyInfoRequestDto.SaveRequest minusChurnUser = HourlyInfoRequestDto.SaveRequest.builder()
				.date(LocalDate.parse(LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
				.hour(24)
				.newUser(10L)
				.churnUser(-1L)
				.payAmount(BigDecimal.valueOf(10L))
				.cost(BigDecimal.valueOf(10L))
				.salesAmount(BigDecimal.valueOf(10L))
				.build();

			String zeroChurnUserData = objectMapper.writeValueAsString(zeroChurnUser);
			String minusChurnUserData = objectMapper.writeValueAsString(minusChurnUser);

			mock.perform(
					post("/api/hourly-data")
						.contentType(MediaType.APPLICATION_JSON)
						.content(zeroChurnUserData)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(status().isOk());

			mock.perform(
					post("/api/hourly-data")
						.contentType(MediaType.APPLICATION_JSON)
						.content(minusChurnUserData)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());
		}

		@DisplayName("유효성 검증 실패 - 매출")
		@Test
		void invalidPayAmountSaveFail() throws Exception {

			HourlyInfoRequestDto.SaveRequest zeroPayAmount = HourlyInfoRequestDto.SaveRequest.builder()
				.date(LocalDate.parse("2022-12-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")))
				.hour(0)
				.newUser(10L)
				.churnUser(12L)
				.payAmount(BigDecimal.ZERO)
				.cost(BigDecimal.valueOf(10L))
				.salesAmount(BigDecimal.valueOf(10L))
				.build();

			HourlyInfoRequestDto.SaveRequest minusPayAmount = HourlyInfoRequestDto.SaveRequest.builder()
				.date(LocalDate.parse(LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
				.hour(24)
				.newUser(10L)
				.churnUser(12L)
				.payAmount(BigDecimal.valueOf(-1L))
				.cost(BigDecimal.valueOf(10L))
				.salesAmount(BigDecimal.valueOf(10L))
				.build();

			String zeroPayAmountData = objectMapper.writeValueAsString(zeroPayAmount);
			String minusPayAmountData = objectMapper.writeValueAsString(minusPayAmount);

			mock.perform(
					post("/api/hourly-data")
						.contentType(MediaType.APPLICATION_JSON)
						.content(zeroPayAmountData)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(status().isOk());

			mock.perform(
					post("/api/hourly-data")
						.contentType(MediaType.APPLICATION_JSON)
						.content(minusPayAmountData)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());
		}

		@DisplayName("유효성 검증 실패 - 비용")
		@Test
		void invalidCostSaveFail() throws Exception {

			HourlyInfoRequestDto.SaveRequest zeroCost = HourlyInfoRequestDto.SaveRequest.builder()
				.date(LocalDate.parse("2022-12-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")))
				.hour(0)
				.newUser(10L)
				.churnUser(12L)
				.payAmount(BigDecimal.valueOf(10L))
				.cost(BigDecimal.ZERO)
				.salesAmount(BigDecimal.valueOf(10L))
				.build();

			HourlyInfoRequestDto.SaveRequest minusCost = HourlyInfoRequestDto.SaveRequest.builder()
				.date(LocalDate.parse(LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
				.hour(24)
				.newUser(10L)
				.churnUser(12L)
				.payAmount(BigDecimal.valueOf(10L))
				.cost(BigDecimal.valueOf(-1L))
				.salesAmount(BigDecimal.valueOf(10L))
				.build();

			String zeroCostData = objectMapper.writeValueAsString(zeroCost);
			String miunsCostData = objectMapper.writeValueAsString(minusCost);

			mock.perform(
					post("/api/hourly-data")
						.contentType(MediaType.APPLICATION_JSON)
						.content(zeroCostData)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(status().isOk());

			mock.perform(
					post("/api/hourly-data")
						.contentType(MediaType.APPLICATION_JSON)
						.content(miunsCostData)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());
		}

		@DisplayName("유효성 검증 실패 - 판매량")
		@Test
		void invalidSalesAmountSaveFail() throws Exception {

			HourlyInfoRequestDto.SaveRequest zeroSalesAmount = HourlyInfoRequestDto.SaveRequest.builder()
				.date(LocalDate.parse("2022-12-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")))
				.hour(0)
				.newUser(10L)
				.churnUser(12L)
				.payAmount(BigDecimal.valueOf(10L))
				.cost(BigDecimal.valueOf(10L))
				.salesAmount(BigDecimal.ZERO)
				.build();

			HourlyInfoRequestDto.SaveRequest minusSalesAmount = HourlyInfoRequestDto.SaveRequest.builder()
				.date(LocalDate.parse(LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
				.hour(24)
				.newUser(10L)
				.churnUser(12L)
				.payAmount(BigDecimal.valueOf(10L))
				.cost(BigDecimal.valueOf(10L))
				.salesAmount(BigDecimal.valueOf(-1L))
				.build();

			String zeroSalesAmountData = objectMapper.writeValueAsString(zeroSalesAmount);
			String minusSalesAmountData = objectMapper.writeValueAsString(minusSalesAmount);

			mock.perform(
					post("/api/hourly-data")
						.contentType(MediaType.APPLICATION_JSON)
						.content(zeroSalesAmountData)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(status().isOk());

			mock.perform(
					post("/api/hourly-data")
						.contentType(MediaType.APPLICATION_JSON)
						.content(minusSalesAmountData)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());
		}
	}

	@DisplayName("수정")
	@Nested
	class Update {

		@DisplayName("성공")
		@Test
		void updateSuccess() throws Exception {

			HourlyInfoRequestDto.UpdateRequest updateData = HourlyInfoRequestDto.UpdateRequest.builder()
				.date(LocalDate.parse("2022-12-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")))
				.hour(0)
				.newUser(10L)
				.churnUser(12L)
				.payAmount(BigDecimal.valueOf(10L))
				.cost(BigDecimal.valueOf(10L))
				.salesAmount(BigDecimal.valueOf(10L))
				.build();

			String updateStr = objectMapper.writeValueAsString(updateData);
			byte[] updateBytes = objectMapper.writeValueAsBytes(updateData);

			mock.perform(
					put("/api/hourly-data")
						.contentType(MediaType.APPLICATION_JSON)
						.content(updateStr)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(status().isOk());

			mock.perform(
					put("/api/hourly-data")
						.contentType(MediaType.APPLICATION_JSON)
						.content(updateBytes)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(status().isOk());
		}

		@DisplayName("필수값 누락 - 날짜")
		@Test
		void withoutDateUpdateFail() throws Exception {

			HourlyInfoRequestDto.UpdateRequest updateData = HourlyInfoRequestDto.UpdateRequest.builder()
				.hour(0)
				.newUser(10L)
				.churnUser(12L)
				.payAmount(BigDecimal.valueOf(10L))
				.cost(BigDecimal.valueOf(10L))
				.salesAmount(BigDecimal.valueOf(10L))
				.build();

			String updateStr = objectMapper.writeValueAsString(updateData);

			mock.perform(
					put("/api/hourly-data")
						.contentType(MediaType.APPLICATION_JSON)
						.content(updateStr)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

		}

		@DisplayName("필수값 누락 - 시간")
		@Test
		void withoutHourUpdateFail() throws Exception {
			HourlyInfoRequestDto.UpdateRequest updateData = HourlyInfoRequestDto.UpdateRequest.builder()
				.date(LocalDate.parse("2022-12-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")))
				.newUser(10L)
				.churnUser(12L)
				.payAmount(BigDecimal.valueOf(10L))
				.cost(BigDecimal.valueOf(10L))
				.salesAmount(BigDecimal.valueOf(10L))
				.build();

			String updateStr = objectMapper.writeValueAsString(updateData);

			mock.perform(
					put("/api/hourly-data")
						.contentType(MediaType.APPLICATION_JSON)
						.content(updateStr)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

		}

		@DisplayName("유효성 검증 실패 - 날짜")
		@Test
		void invalidDateUpdateFail() throws Exception {
			HourlyInfoRequestDto.UpdateRequest futureData = HourlyInfoRequestDto.UpdateRequest.builder()
				.date(LocalDate.parse(LocalDate.now().plusDays(1).format( DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
				.hour(0)
				.newUser(10L)
				.churnUser(12L)
				.payAmount(BigDecimal.valueOf(10L))
				.cost(BigDecimal.valueOf(10L))
				.salesAmount(BigDecimal.valueOf(10L))
				.build();

			String updateStr = objectMapper.writeValueAsString(futureData);

			mock.perform(
					put("/api/hourly-data")
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

			HourlyInfoRequestDto.UpdateRequest underZeroHour = HourlyInfoRequestDto.UpdateRequest.builder()
					.date(LocalDate.parse(LocalDate.now().plusDays(1).format( DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
				.date(LocalDate.parse("2022-12-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")))
				.hour(-1)
				.newUser(10L)
				.churnUser(12L)
				.payAmount(BigDecimal.valueOf(10L))
				.cost(BigDecimal.valueOf(10L))
				.salesAmount(BigDecimal.valueOf(10L))
				.build();

			HourlyInfoRequestDto.UpdateRequest overMaxHour = HourlyInfoRequestDto.UpdateRequest.builder()
				.date(LocalDate.parse("2022-12-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")))
				.hour(24)
				.newUser(10L)
				.churnUser(12L)
				.payAmount(BigDecimal.valueOf(10L))
				.cost(BigDecimal.valueOf(10L))
				.salesAmount(BigDecimal.valueOf(10L))
				.build();

			String underZeroData= objectMapper.writeValueAsString(underZeroHour);
			String overMaxData= objectMapper.writeValueAsString(overMaxHour);

			mock.perform(
					put("/api/hourly-data")
						.contentType(MediaType.APPLICATION_JSON)
						.content(underZeroData)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

			mock.perform(
					put("/api/hourly-data")
						.contentType(MediaType.APPLICATION_JSON)
						.content(overMaxData)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());
		}

		@DisplayName("유효성 검증 실패 - 신규 유저")
		@Test
		void invalidNewUserUpdateFail() throws Exception {

			HourlyInfoRequestDto.UpdateRequest zeroNewUser = HourlyInfoRequestDto.UpdateRequest.builder()
				.date(LocalDate.parse("2022-12-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")))
				.hour(0)
				.newUser(0L)
				.churnUser(12L)
				.payAmount(BigDecimal.valueOf(10L))
				.cost(BigDecimal.valueOf(10L))
				.salesAmount(BigDecimal.valueOf(10L))
				.build();

			HourlyInfoRequestDto.UpdateRequest minusNewUser = HourlyInfoRequestDto.UpdateRequest.builder()
				.date(LocalDate.parse(LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
				.hour(24)
				.newUser(-1L)
				.churnUser(12L)
				.payAmount(BigDecimal.valueOf(10L))
				.cost(BigDecimal.valueOf(10L))
				.salesAmount(BigDecimal.valueOf(10L))
				.build();

			String zeroNewUserData = objectMapper.writeValueAsString(zeroNewUser);
			String minusNewUserData = objectMapper.writeValueAsString(minusNewUser);

			mock.perform(
					put("/api/hourly-data")
						.contentType(MediaType.APPLICATION_JSON)
						.content(zeroNewUserData)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(status().isOk());

			mock.perform(
					put("/api/hourly-data")
						.contentType(MediaType.APPLICATION_JSON)
						.content(minusNewUserData)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());
		}

		@DisplayName("유효성 검증 실패 - 이탈 유저")
		@Test
		void invalidChurnUserUpdateFail() throws Exception {

			HourlyInfoRequestDto.UpdateRequest zeroChurnUser = HourlyInfoRequestDto.UpdateRequest.builder()
				.date(LocalDate.parse("2022-12-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")))
				.hour(0)
				.newUser(1L)
				.churnUser(0L)
				.payAmount(BigDecimal.valueOf(10L))
				.cost(BigDecimal.valueOf(10L))
				.salesAmount(BigDecimal.valueOf(10L))
				.build();

			HourlyInfoRequestDto.UpdateRequest minusChurnUser = HourlyInfoRequestDto.UpdateRequest.builder()
				.date(LocalDate.parse(LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
				.hour(24)
				.newUser(10L)
				.churnUser(-1L)
				.payAmount(BigDecimal.valueOf(10L))
				.cost(BigDecimal.valueOf(10L))
				.salesAmount(BigDecimal.valueOf(10L))
				.build();

			String zeroChurnUserData = objectMapper.writeValueAsString(zeroChurnUser);
			String minusChurnUserData = objectMapper.writeValueAsString(minusChurnUser);

			mock.perform(
					put("/api/hourly-data")
						.contentType(MediaType.APPLICATION_JSON)
						.content(zeroChurnUserData)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(status().isOk());

			mock.perform(
					put("/api/hourly-data")
						.contentType(MediaType.APPLICATION_JSON)
						.content(minusChurnUserData)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());
		}

		@DisplayName("유효성 검증 실패 - 매출")
		@Test
		void invalidPayAmountUpdateFail() throws Exception {

			HourlyInfoRequestDto.UpdateRequest zeroPayAmount = HourlyInfoRequestDto.UpdateRequest.builder()
				.date(LocalDate.parse("2022-12-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")))
				.hour(0)
				.newUser(10L)
				.churnUser(12L)
				.payAmount(BigDecimal.ZERO)
				.cost(BigDecimal.valueOf(10L))
				.salesAmount(BigDecimal.valueOf(10L))
				.build();

			HourlyInfoRequestDto.UpdateRequest minusPayAmount = HourlyInfoRequestDto.UpdateRequest.builder()
				.date(LocalDate.parse(LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
				.hour(24)
				.newUser(10L)
				.churnUser(12L)
				.payAmount(BigDecimal.valueOf(-1L))
				.cost(BigDecimal.valueOf(10L))
				.salesAmount(BigDecimal.valueOf(10L))
				.build();

			String zeroPayAmountData = objectMapper.writeValueAsString(zeroPayAmount);
			String minusPayAmountData = objectMapper.writeValueAsString(minusPayAmount);

			mock.perform(
					put("/api/hourly-data")
						.contentType(MediaType.APPLICATION_JSON)
						.content(zeroPayAmountData)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(status().isOk());

			mock.perform(
					put("/api/hourly-data")
						.contentType(MediaType.APPLICATION_JSON)
						.content(minusPayAmountData)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());
		}

		@DisplayName("유효성 검증 실패 - 비용")
		@Test
		void invalidCostUpdateFail() throws Exception {

			HourlyInfoRequestDto.UpdateRequest zeroCost = HourlyInfoRequestDto.UpdateRequest.builder()
				.date(LocalDate.parse("2022-12-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")))
				.hour(0)
				.newUser(10L)
				.churnUser(12L)
				.payAmount(BigDecimal.valueOf(10L))
				.cost(BigDecimal.ZERO)
				.salesAmount(BigDecimal.valueOf(10L))
				.build();

			HourlyInfoRequestDto.UpdateRequest minusCost = HourlyInfoRequestDto.UpdateRequest.builder()
				.date(LocalDate.parse(LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
				.hour(24)
				.newUser(10L)
				.churnUser(12L)
				.payAmount(BigDecimal.valueOf(10L))
				.cost(BigDecimal.valueOf(-1L))
				.salesAmount(BigDecimal.valueOf(10L))
				.build();

			String zeroCostData = objectMapper.writeValueAsString(zeroCost);
			String miunsCostData = objectMapper.writeValueAsString(minusCost);

			mock.perform(
					put("/api/hourly-data")
						.contentType(MediaType.APPLICATION_JSON)
						.content(zeroCostData)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(status().isOk());

			mock.perform(
					put("/api/hourly-data")
						.contentType(MediaType.APPLICATION_JSON)
						.content(miunsCostData)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());
		}

		@DisplayName("유효성 검증 실패 - 판매량")
		@Test
		void invalidSalesAmountUpdateFail() throws Exception {

			HourlyInfoRequestDto.UpdateRequest zeroSalesAmount = HourlyInfoRequestDto.UpdateRequest.builder()
				.date(LocalDate.parse("2022-12-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")))
				.hour(0)
				.newUser(10L)
				.churnUser(12L)
				.payAmount(BigDecimal.valueOf(10L))
				.cost(BigDecimal.valueOf(10L))
				.salesAmount(BigDecimal.ZERO)
				.build();

			HourlyInfoRequestDto.UpdateRequest minusSalesAmount = HourlyInfoRequestDto.UpdateRequest.builder()
				.date(LocalDate.parse(LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
				.hour(24)
				.newUser(10L)
				.churnUser(12L)
				.payAmount(BigDecimal.valueOf(10L))
				.cost(BigDecimal.valueOf(10L))
				.salesAmount(BigDecimal.valueOf(-1L))
				.build();

			String zeroSalesAmountData = objectMapper.writeValueAsString(zeroSalesAmount);
			String minusSalesAmountData = objectMapper.writeValueAsString(minusSalesAmount);

			mock.perform(
					put("/api/hourly-data")
						.contentType(MediaType.APPLICATION_JSON)
						.content(zeroSalesAmountData)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(status().isOk());

			mock.perform(
					put("/api/hourly-data")
						.contentType(MediaType.APPLICATION_JSON)
						.content(minusSalesAmountData)
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());
		}
	}

	@Nested
	@DisplayName("삭제")
	class Delete {

		@DisplayName("성공")
		@Test
		void deleteSuccess() throws Exception {

			mock.perform(
					delete("/api/hourly-data")
						.contentType(MediaType.APPLICATION_JSON)
						.param("date", "2022-12-01")
						.param("hour", "0")
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(status().isOk());
		}

		@DisplayName("필수값 누락 - 날짜")
		@Test
		void noDate() throws Exception {

			mock.perform(
					delete("/api/hourly-data")
						.contentType(MediaType.APPLICATION_JSON)
						.param("hour", "0")
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());
		}

		@DisplayName("필수값 누락 - 시간")
		@Test
		void noHour() throws Exception {

			mock.perform(
					delete("/api/hourly-data")
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
					delete("/api/hourly-data")
						.contentType(MediaType.APPLICATION_JSON)
						.param("date", LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
						.param("hour", "0")
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());
		}

		@DisplayName("유효성 검증 실패 - 시간")
		@Test
		void invalidHourDeleteFail() throws Exception {

			mock.perform(
					delete("/api/hourly-data")
						.contentType(MediaType.APPLICATION_JSON)
						.param("date", LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
						.param("hour", "-1")
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());

			mock.perform(
					delete("/api/hourly-data")
						.contentType(MediaType.APPLICATION_JSON)
						.param("date", LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
						.param("hour", "24")
						.characterEncoding(StandardCharsets.UTF_8)
				).andDo(print())
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
				.andExpect(status().isBadRequest());
		}

	}
}