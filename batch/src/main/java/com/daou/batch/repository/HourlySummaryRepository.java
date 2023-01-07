package com.daou.batch.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daou.batch.model.HourlySummary;

public interface HourlySummaryRepository extends JpaRepository<HourlySummary, Long> {
	void deleteByCreatedDate(LocalDate createDate);
}
