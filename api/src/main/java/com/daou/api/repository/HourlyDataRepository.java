package com.daou.api.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daou.api.model.HourlyData;

public interface HourlyDataRepository extends JpaRepository<HourlyData, Long>, HourlyDataRepositoryCustom {

	void deleteByCreatedDate(LocalDate createDate);

	void deleteByDate(LocalDate date);

	Optional<HourlyData> findByDateAndHour(LocalDate date, int hour);
}