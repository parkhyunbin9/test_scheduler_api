package com.daou.api.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daou.api.model.DailySummary;

public interface DailySummaryRepository extends JpaRepository<DailySummary, Long>, DailySummaryRepositoryCustom {

	void deleteByCreatedDate(LocalDate createDate);

	Optional<DailySummary> findByDate(LocalDate date);
}
