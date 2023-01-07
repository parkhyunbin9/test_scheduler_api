package com.daou.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daou.api.model.HourlySummary;

public interface HourlySummaryRepository extends JpaRepository<HourlySummary, Long>, HourlySummaryRepositoryCustom {

	Optional<HourlySummary> findByHour(int hour);
}
