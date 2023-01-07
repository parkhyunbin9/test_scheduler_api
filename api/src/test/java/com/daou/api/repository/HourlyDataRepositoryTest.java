package com.daou.api.repository;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.util.BigDecimalComparator.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.daou.api.dto.request.HourlyInfoRequestDto;
import com.daou.api.model.HourlyData;

@SpringBootTest
@Transactional
class HourlyDataRepositoryTest {

	@Autowired
	HourlyDataRepository repository;
	@Autowired
	EntityManager em;

	@Test
	void 저장_hourlyData() {
		// given
		HourlyInfoRequestDto.SaveRequest hourlyDataSaveReq = new HourlyInfoRequestDto.SaveRequest(
			LocalDate.of(2022, 01, 01), 0, 0L, 0L, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
		HourlyData newHourlyData = hourlyDataSaveReq.toEntity();

		// when
		HourlyData savedData = repository.save(newHourlyData);
		repository.flush();

		// then
		assertThat(savedData).isSameAs(newHourlyData);

		assertThat(savedData)
			.usingComparatorForType(BIG_DECIMAL_COMPARATOR, BigDecimal.class)
			.usingRecursiveComparison()
			.isEqualTo(newHourlyData);
	}

	@Test
	void 저장된_데이터_조회() {
		// given
		HourlyInfoRequestDto.SaveRequest hourlyDataSaveReq = new HourlyInfoRequestDto.SaveRequest(
			LocalDate.of(2022, 01, 01), 0, 0L, 0L, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
		HourlyInfoRequestDto.SaveRequest hourlyDataSaveReq2 = new HourlyInfoRequestDto.SaveRequest(
			LocalDate.of(2022, 01, 02), 1, 1L, 1L, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE);
		HourlyData newHourlyData = hourlyDataSaveReq.toEntity();
		HourlyData newHourlyData2 = hourlyDataSaveReq2.toEntity();
		HourlyData savedData = repository.save(newHourlyData);
		HourlyData savedData2 = repository.save(newHourlyData2);
		repository.flush();
		em.clear();

		// when
		HourlyData findData = repository.findById(savedData.getId())
			.orElseThrow(() -> new IllegalArgumentException(
				"Wrong HourlyData date : " + savedData.getDate() + " hour : " + savedData.getHour()));
		HourlyData findData2 = repository.findById(savedData2.getId())
			.orElseThrow(() -> new IllegalArgumentException(
				"Wrong HourlyData2 date : " + savedData2.getDate() + " hour : " + savedData2.getHour()));

		// then
		assertThat(repository.count()).isEqualTo(2);
		assertThat(findData)
			.usingComparatorForType(BIG_DECIMAL_COMPARATOR, BigDecimal.class)
			.usingRecursiveComparison()
			.isEqualTo(savedData);
		assertThat(findData2)
			.usingComparatorForType(BIG_DECIMAL_COMPARATOR, BigDecimal.class)
			.usingRecursiveComparison()
			.isEqualTo(savedData2);
	}

	@Test
	void 수정_hourlyData() {
		// given
		HourlyInfoRequestDto.SaveRequest hourlyDataSaveReq = new HourlyInfoRequestDto.SaveRequest(
			LocalDate.of(2022, 01, 01), 0, 0L, 0L, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
		HourlyData newHourlyData = hourlyDataSaveReq.toEntity();
		HourlyData savedData = repository.save(newHourlyData);
		HourlyData beUpdateData = new HourlyData(LocalDate.of(2022, 01, 02), 10, 15L, 15L, BigDecimal.TEN,
			BigDecimal.TEN, BigDecimal.TEN);
		repository.flush();
		em.clear();

		// when
		HourlyData findData = repository.findByDateAndHour(savedData.getDate(), savedData.getHour())
			.orElseThrow(() -> new IllegalArgumentException("요청하신 날짜와 시간에 해당하는 데이터가 없습니다"));
		findData.change(beUpdateData);
		repository.flush();

		HourlyData updatedData = repository.findByDateAndHour(savedData.getDate(), savedData.getHour())
			.orElseThrow(() -> new IllegalArgumentException("요청하신 날짜와 시간에 해당하는 데이터가 없습니다"));

		// then
		assertThat(savedData.getId()).isEqualTo(updatedData.getId());
		assertThat(updatedData.getDate()).isEqualTo(savedData.getDate());
		assertThat(updatedData.getHour()).isEqualTo(savedData.getHour());
		assertThat(updatedData.getNewUser()).isNotEqualTo(savedData.getNewUser());
		assertThat(updatedData.getChurnUser()).isNotEqualTo(savedData.getChurnUser());
		assertThat(updatedData.getPayAmount()).isNotEqualByComparingTo(savedData.getPayAmount());
		assertThat(updatedData.getCost()).isNotEqualByComparingTo(savedData.getCost());
		assertThat(updatedData.getSalesAmount()).isNotEqualByComparingTo(savedData.getSalesAmount());

		assertThatThrownBy(() -> repository.findByDateAndHour(beUpdateData.getDate(), savedData.getHour())
			.orElseThrow(() -> new IllegalArgumentException("요청하신 날짜와 시간에 해당하는 데이터가 없습니다")))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void 삭제_hourlyData() {
		// given
		HourlyInfoRequestDto.SaveRequest hourlyDataSaveReq = new HourlyInfoRequestDto.SaveRequest(
			LocalDate.of(2022, 01, 01), 0, 0L, 0L, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
		HourlyData newHourlyData = hourlyDataSaveReq.toEntity();

		// when
		HourlyData savedData = repository.save(newHourlyData);
		repository.flush();

		HourlyData findData = repository.findByDateAndHour(hourlyDataSaveReq.getDate(), hourlyDataSaveReq.getHour())
			.orElseThrow(() -> new IllegalArgumentException("요청하신 날짜와 시간에 해당하는 데이터가 없습니다."));

		repository.delete(findData);
		repository.flush();
		em.clear();

		// then
		assertThatThrownBy(() ->
			repository.findByDateAndHour(hourlyDataSaveReq.getDate(), hourlyDataSaveReq.getHour())
				.orElseThrow(() -> new IllegalArgumentException())
		).isInstanceOf(IllegalArgumentException.class);
	}

}