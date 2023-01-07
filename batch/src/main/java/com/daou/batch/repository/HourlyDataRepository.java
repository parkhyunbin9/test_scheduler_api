package com.daou.batch.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daou.batch.model.HourlyData;

public interface HourlyDataRepository extends JpaRepository<HourlyData, Long> {
	void deleteByCreatedDate(LocalDate createDate);
}
