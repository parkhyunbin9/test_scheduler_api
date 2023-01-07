package com.daou.api.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.daou.api.common.spec.CommonException;
import com.daou.api.common.spec.ExceptionCode;
import com.daou.api.dto.request.DailySummaryRequestDto;
import com.daou.api.dto.request.HourlySummaryRequestDto;
import com.daou.api.dto.response.SummaryResponseDto;
import com.daou.api.model.DailySummary;
import com.daou.api.model.HourlySummary;
import com.daou.api.repository.DailySummaryRepository;
import com.daou.api.repository.HourlySummaryRepository;

@ExtendWith(MockitoExtension.class)
class SummaryCommandServiceTest {

	@InjectMocks
	SummaryCommandService summaryCommandService;

	@Mock
	HourlySummaryRepository hourlySummaryRepository;

	@Mock
	DailySummaryRepository dailySummaryRepository;

	@DisplayName("성공")
	@Nested
	class Success {

		@DisplayName("시간별 요약 집계 저장")
		@Test
		void hourlySaveSuccess() {

			// given
			HourlySummaryRequestDto.SaveRequest saveData = new HourlySummaryRequestDto.SaveRequest(
				0,
				10L, 10L, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE,
				10D, 10D, 10D, 10D, 10D,
				10L, 10L, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE,
				10L, 10L, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE);

			when(hourlySummaryRepository.save(saveData.toEntity())).thenReturn(saveData.toEntity());

			// when
			SummaryResponseDto.Hourly savedData = summaryCommandService.saveHourlySummary(saveData);

			//then
			assertThat(savedData).usingRecursiveComparison()
				.isEqualTo(SummaryResponseDto.Hourly.fromEntity(saveData.toEntity()));

		}

		@DisplayName("날짜별 요약 집계 저장")
		@Test
		void dailySaveSuccess() {

			// given
			DailySummaryRequestDto.SaveRequest saveData = new DailySummaryRequestDto.SaveRequest(
				LocalDate.of(2022, 01, 01),
				10L, 10L, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE,
				10D, 10D, 10D, 10D, 10D,
				10L, 10L, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE,
				10L, 10L, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE
			);

			when(dailySummaryRepository.save(saveData.toEntity()))
				.thenReturn(saveData.toEntity());

			// when
			SummaryResponseDto.Daily savedData = summaryCommandService.saveDailySummary(saveData);

			//then
			assertThat(savedData).usingRecursiveComparison()
				.isEqualTo(SummaryResponseDto.Daily.fromEntity(saveData.toEntity()));

		}

		@DisplayName("시간별 요약 데이터 수정")
		@Test
		void hourlyUpdateSuccess() {

			// given
			HourlySummaryRequestDto.SaveRequest originData = new HourlySummaryRequestDto.SaveRequest(
				0,
				10L, 10L, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE,
				10D, 10D, 10D, 10D, 10D,
				10L, 10L, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE,
				10L, 10L, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE);

			when(hourlySummaryRepository.findByHour(anyInt()))
				.thenReturn(Optional.of(originData.toEntity()));

			Long changeNewUser = 100L;
			Long changeChurnUser = 100L;
			BigDecimal changePayAmount = BigDecimal.valueOf(1000L);
			BigDecimal changeCost = BigDecimal.valueOf(1000L);
			BigDecimal changeSalesAmount = BigDecimal.valueOf(1000L);

			HourlySummaryRequestDto.UpdateRequest changeData = new HourlySummaryRequestDto.UpdateRequest(
				0,
				changeNewUser, changeChurnUser, changePayAmount, changeCost, changeSalesAmount,
				10D, 10D, 10D, 10D, 10D,
				10L, 10L, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN,
				10L, 10L, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN
			);

			// when
			SummaryResponseDto.Hourly updatedData = summaryCommandService.updateHourlySummary(changeData);

			//then
			assertThat(updatedData.getSumNewUser()).isEqualTo(changeNewUser);
			assertThat(updatedData.getSumChurnUser()).isEqualTo(changeChurnUser);
			assertThat(updatedData.getSumPayAmount()).isEqualTo(changePayAmount);
			assertThat(updatedData.getSumCost()).isEqualTo(changePayAmount);
			assertThat(updatedData.getSumSalesAmount()).isEqualTo(changeSalesAmount);

		}

		@DisplayName("날짜별 요약 데이터 수정")
		@Test
		void dailyUpdateSuccess() {

			// given
			DailySummaryRequestDto.SaveRequest originData = new DailySummaryRequestDto.SaveRequest(
				LocalDate.of(2022, 01, 01),
				10L, 10L, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE,
				10D, 10D, 10D, 10D, 10D,
				10L, 10L, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE,
				10L, 10L, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE
			);

			when(dailySummaryRepository.findByDate(any(LocalDate.class)))
				.thenReturn(Optional.of(originData.toEntity()));

			LocalDate unChangedDate = LocalDate.of(1999, 01, 01);
			Integer unChangedHour = 23;
			Long changeNewUser = 100L;
			Long changeChurnUser = 100L;
			BigDecimal changePayAmount = BigDecimal.valueOf(1000L);
			BigDecimal changeCost = BigDecimal.valueOf(1000L);
			BigDecimal changeSalesAmount = BigDecimal.valueOf(1000L);

			DailySummaryRequestDto.UpdateRequest changeData = new DailySummaryRequestDto.UpdateRequest(
				LocalDate.of(2022, 01, 01),
				changeNewUser, changeChurnUser, changePayAmount, changeCost, changeSalesAmount,
				10D, 10D, 10D, 10D, 10D,
				10L, 10L, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN,
				10L, 10L, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN
			);

			// when
			SummaryResponseDto.Daily updatedData = summaryCommandService.updateDailySummary(
				changeData);

			//then
			assertThat(updatedData.getSumNewUser()).isEqualTo(changeNewUser);
			assertThat(updatedData.getSumChurnUser()).isEqualTo(changeChurnUser);
			assertThat(updatedData.getSumPayAmount()).isEqualTo(changePayAmount);
			assertThat(updatedData.getSumCost()).isEqualTo(changePayAmount);
			assertThat(updatedData.getSumSalesAmount()).isEqualTo(changeSalesAmount);

		}

		@DisplayName("시간별 요약 데이터 삭제")
		@Test
		void hourlyDeleteSuccess() {

			// given
			HourlySummaryRequestDto.DeleteRequest deleteRequest = new HourlySummaryRequestDto.DeleteRequest(0);

			Optional<HourlySummary> deleteData = Optional.of(HourlySummary.builder()
				.hour(deleteRequest.getHour())
				.sumNewUser(0L)
				.sumChurnUser(0L)
				.sumPayAmount(BigDecimal.TEN)
				.sumCost(BigDecimal.TEN)
				.sumSalesAmount(BigDecimal.TEN)
				.build());

			when(hourlySummaryRepository.findByHour(deleteRequest.getHour()))
				.thenReturn(deleteData);

			// when
			summaryCommandService.deleteHourlySummary(deleteRequest);

			//then
			verify(hourlySummaryRepository).delete(deleteData.get());

		}

		@DisplayName("요일별 요약 데이터 삭제")
		@Test
		void dailyDeleteSuccess() {

			// given
			DailySummaryRequestDto.DeleteRequest deleteRequest = new DailySummaryRequestDto.DeleteRequest(
				LocalDate.of(2022, 01, 01));

			Optional<DailySummary> deleteData = Optional.of(DailySummary.builder()
				.date(deleteRequest.getDate())
				.sumNewUser(0L)
				.sumChurnUser(0L)
				.sumPayAmount(BigDecimal.TEN)
				.sumCost(BigDecimal.TEN)
				.sumSalesAmount(BigDecimal.TEN)
				.build());

			when(dailySummaryRepository.findByDate(deleteRequest.getDate()))
				.thenReturn(deleteData);

			// when
			summaryCommandService.deleteDailySummary(deleteRequest);

			//then
			verify(dailySummaryRepository, times(1)).delete(deleteData.get());

		}
	}

	@DisplayName("실패")
	@Nested
	class Fail {

		@DisplayName("시간별 요약 데이터 저장 실패 - 이미 집계된 시간 저장할 수 없음 ")
		@Test
		void hourlySaveFail() {

			// given
			HourlySummaryRequestDto.SaveRequest saveData = new HourlySummaryRequestDto.SaveRequest(
				0,
				10L, 10L, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE,
				10D, 10D, 10D, 10D, 10D,
				10L, 10L, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE,
				10L, 10L, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE);

			when(hourlySummaryRepository.findByHour(anyInt()))
				.thenReturn(Optional.of(saveData.toEntity()));

			// when & then
			assertThatThrownBy(() ->
				summaryCommandService.saveHourlySummary(saveData))
				.isInstanceOf(CommonException.class);

			assertThatThrownBy(() ->
				summaryCommandService.saveHourlySummary(saveData))
				.hasMessage(ExceptionCode.ALREADY_AGGREGATED_DATA.getErrorCode());
		}

		@DisplayName("일별 요약 데이터 저장 실패 - 이미 집계된 날짜")
		@Test
		void dailySaveFail() {

			// given
			DailySummaryRequestDto.SaveRequest saveData = new DailySummaryRequestDto.SaveRequest(
				LocalDate.of(2022, 01, 01),
				10L, 10L, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE,
				10D, 10D, 10D, 10D, 10D,
				10L, 10L, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE,
				10L, 10L, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE
			);
			when(dailySummaryRepository.findByDate(any(LocalDate.class)))
				.thenReturn(Optional.of(saveData.toEntity()));

			// when & then
			assertThatThrownBy(() ->
				summaryCommandService.saveDailySummary(saveData))
				.isInstanceOf(CommonException.class);

			assertThatThrownBy(() ->
				summaryCommandService.saveDailySummary(saveData))
				.hasMessage(ExceptionCode.ALREADY_AGGREGATED_DATA.getErrorCode());
		}

		@DisplayName("시간별 요약 데이터 수정 실패 - 수정 요청 데이터 없음 ")
		@Test
		void hourlyUpdateFail() {

			// given
			HourlySummaryRequestDto.UpdateRequest updateData = new HourlySummaryRequestDto.UpdateRequest(
				0,
				10L, 10L, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE,
				10D, 10D, 10D, 10D, 10D,
				10L, 10L, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE,
				10L, 10L, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE
			);

			when(hourlySummaryRepository.findByHour(anyInt()))
				.thenReturn(Optional.empty());

			// when & then
			assertThatThrownBy(() ->
				summaryCommandService.updateHourlySummary(updateData))
				.isInstanceOf(CommonException.class);

			assertThatThrownBy(() ->
				summaryCommandService.updateHourlySummary(updateData))
				.hasMessage(ExceptionCode.NOT_FOUND.getErrorCode());
		}

		@DisplayName("일별 요약 데이터 수정 실패 - 수정 요청 데이터 없음 ")
		@Test
		void dailyUpdateFail() {

			// given
			DailySummaryRequestDto.UpdateRequest updateData = new DailySummaryRequestDto.UpdateRequest(
				LocalDate.of(2022, 01, 01),
				10L, 10L, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE,
				10D, 10D, 10D, 10D, 10D,
				10L, 10L, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE,
				10L, 10L, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE
			);

			when(dailySummaryRepository.findByDate(any(LocalDate.class)))
				.thenReturn(Optional.empty());

			// when & then
			assertThatThrownBy(() ->
				summaryCommandService.updateDailySummary(updateData))
				.isInstanceOf(CommonException.class);

			assertThatThrownBy(() ->
				summaryCommandService.updateDailySummary(updateData))
				.hasMessage(ExceptionCode.NOT_FOUND.getErrorCode());
		}

		@DisplayName("시간별 요약 데이터 삭제 실패 - 삭제 요청 데이터 없음 ")
		@Test
		void hourlyDeleteFail() {

			// given
			when(hourlySummaryRepository.findByHour(anyInt()))
				.thenReturn(Optional.empty());

			// when & then
			assertThatThrownBy(() ->
				summaryCommandService.deleteHourlySummary(
					new HourlySummaryRequestDto.DeleteRequest(0))
			).isInstanceOf(CommonException.class);

			assertThatThrownBy(() ->
				summaryCommandService.deleteHourlySummary(
					new HourlySummaryRequestDto.DeleteRequest(0))
			).hasMessage(ExceptionCode.NOT_FOUND.getErrorCode());

		}

		@DisplayName("일별 요약 데이터 삭제 실패 - 삭제 요청 데이터 없음 ")
		@Test
		void dailyDeleteFail() {

			// given
			when(dailySummaryRepository.findByDate(any(LocalDate.class)))
				.thenReturn(Optional.empty());

			// when & then
			assertThatThrownBy(() ->
				summaryCommandService.deleteDailySummary(
					new DailySummaryRequestDto.DeleteRequest(LocalDate.of(2022, 01, 01)))
			).isInstanceOf(CommonException.class);

			assertThatThrownBy(() ->
				summaryCommandService.deleteDailySummary(
					new DailySummaryRequestDto.DeleteRequest(LocalDate.of(2022, 01, 01))
				)).hasMessage(ExceptionCode.NOT_FOUND.getErrorCode());

		}
	}

}