package com.daou.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daou.api.model.HourlyRowData;

public interface HourlyRowDataRepository extends JpaRepository<HourlyRowData, Long> {
}
