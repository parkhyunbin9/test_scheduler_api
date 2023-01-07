package com.daou.api.service;

import static org.assertj.core.api.Assertions.*;
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
import com.daou.api.dto.request.HourlyInfoRequestDto;
import com.daou.api.dto.response.HourlyDataResponseDto;
import com.daou.api.model.HourlyData;
import com.daou.api.repository.HourlyDataRepository;

@ExtendWith(MockitoExtension.class)
class HourlyCommandServiceTest {

	@InjectMocks
	HourlyCommandService hourlyCommandService;

	@Mock
	HourlyDataRepository hourlyDataRepository;

	@DisplayName("성공")
	@Nested
	class Success {

		@DisplayName("저장")
		@Test
		void saveSuccess() {

			// given
			HourlyInfoRequestDto.SaveRequest saveData = new HourlyInfoRequestDto.SaveRequest(
				LocalDate.of(2022, 01, 01), 0
				, 10L, 10L, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE);

			when(hourlyDataRepository.save(saveData.toEntity()))
				.thenReturn(saveData.toEntity());

			// when
			HourlyDataResponseDto.SaveAndUpdateResponse savedData = hourlyCommandService.saveHourlyData(
				saveData);

			//then
			assertThat(savedData).usingRecursiveComparison()
				.isEqualTo(HourlyDataResponseDto.SaveAndUpdateResponse.fromEntity(saveData.toEntity()));

		}

		@DisplayName("수정")
		@Test
		void updateSuccess() {

			// given
			HourlyInfoRequestDto.UpdateRequest originData = new HourlyInfoRequestDto.UpdateRequest(
				LocalDate.of(2022, 01, 01), 0
				, 10L, 10L, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE);

			when(hourlyDataRepository.findByDateAndHour(any(LocalDate.class), anyInt()))
				.thenReturn(Optional.of(originData.toEntity()));

			LocalDate unChangedDate = LocalDate.of(1999, 01, 01);
			Integer unChangedHour = 23;
			Long changeNewUser = 100L;
			Long changeChurnUser = 100L;
			BigDecimal changePayAmount = BigDecimal.valueOf(1000L);
			BigDecimal changeCost = BigDecimal.valueOf(1000L);
			BigDecimal changeSalesAmount = BigDecimal.valueOf(1000L);

			HourlyInfoRequestDto.UpdateRequest changeData = new HourlyInfoRequestDto.UpdateRequest(unChangedDate,
				unChangedHour, changeNewUser
				, changeChurnUser, changePayAmount, changeCost, changeSalesAmount);

			// when
			HourlyDataResponseDto.SaveAndUpdateResponse updatedData = hourlyCommandService.updateHourlyData(
				changeData);

			//then
			assertThat(updatedData.getNewUser()).isEqualTo(changeNewUser);
			assertThat(updatedData.getChurnUser()).isEqualTo(changeChurnUser);
			assertThat(updatedData.getPayAmount()).isEqualTo(changePayAmount);
			assertThat(updatedData.getCost()).isEqualTo(changePayAmount);
			assertThat(updatedData.getSalesAmount()).isEqualTo(changeSalesAmount);

		}

		@DisplayName("삭제")
		@Test
		void deleteSuccess() {

			// given
			HourlyInfoRequestDto.DeleteRequest deleteRequest = new HourlyInfoRequestDto.DeleteRequest(
				LocalDate.of(2022, 01, 01), 0);

			Optional<HourlyData> deleteData = Optional.of(HourlyData.builder()
				.date(deleteRequest.getDate())
				.hour(deleteRequest.getHour())
				.newUser(0L)
				.churnUser(0L)
				.payAmount(BigDecimal.TEN)
				.cost(BigDecimal.TEN)
				.salesAmount(BigDecimal.TEN)
				.build());

			when(hourlyDataRepository.findByDateAndHour(deleteRequest.getDate(), deleteRequest.getHour()))
				.thenReturn(deleteData);

			// when
			hourlyCommandService.deleteHourlyData(deleteRequest);

			//then
			verify(hourlyDataRepository).delete(deleteData.get());

		}
	}

	@DisplayName("실패")
	@Nested
	class Fail {

		@DisplayName("수정 실패 - 수정 요청 데이터 없음 ")
		@Test
		void updateFail() {

			// given
			HourlyInfoRequestDto.UpdateRequest updateData = new HourlyInfoRequestDto.UpdateRequest(
				LocalDate.of(2022, 01, 01), 0
				, 10L, 10L, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE);

			when(hourlyDataRepository.findByDateAndHour(any(LocalDate.class), anyInt()))
				.thenReturn(Optional.empty());

			// when & then
			assertThatThrownBy(() ->
				hourlyCommandService.updateHourlyData(updateData))
				.isInstanceOf(CommonException.class);

			assertThatThrownBy(() ->
				hourlyCommandService.updateHourlyData(updateData))
				.hasMessage(ExceptionCode.NOT_FOUND.getErrorCode());
		}

		@DisplayName("삭제 실패 - 삭제 요청 데이터 없음 ")
		@Test
		void deleteFail() {

			// given
			when(hourlyDataRepository.findByDateAndHour(any(LocalDate.class), anyInt()))
				.thenReturn(Optional.empty());

			// when & then
			assertThatThrownBy(() ->
				hourlyCommandService.deleteHourlyData(
					new HourlyInfoRequestDto.DeleteRequest(LocalDate.of(2022, 01, 01), 0))
			).isInstanceOf(CommonException.class);

			assertThatThrownBy(() ->
				hourlyCommandService.deleteHourlyData(
					new HourlyInfoRequestDto.DeleteRequest(LocalDate.of(2022, 01, 01), 0))
			).hasMessage(ExceptionCode.NOT_FOUND.getErrorCode());

		}
	}

}

