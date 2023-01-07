package com.daou.api.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daou.api.model.HourlyRowData;

public interface HourlyRowDataRepository extends JpaRepository<HourlyRowData, Long> {
	void deleteByCreatedDate(LocalDate createDate);
}
