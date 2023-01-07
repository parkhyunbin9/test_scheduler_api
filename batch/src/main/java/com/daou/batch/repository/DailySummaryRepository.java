package com.daou.batch.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daou.batch.model.DailySummary;

public interface DailySummaryRepository extends JpaRepository<DailySummary, Long> {
	void deleteByCreatedDate(LocalDate createDate);
}
