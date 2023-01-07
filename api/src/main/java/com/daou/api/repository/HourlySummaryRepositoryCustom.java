package com.daou.api.repository;

import java.util.List;

import com.daou.api.dto.request.search.SearchConditionDto;
import com.daou.api.dto.response.SummaryResponseDto;

public interface HourlySummaryRepositoryCustom {

	List<SummaryResponseDto.Hourly> findHourlySummaryWithCondition(SearchConditionDto condition);
}
