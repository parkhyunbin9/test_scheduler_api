package com.daou.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@EnableAutoConfiguration
@EnableBatchProcessing
@ComponentScan(basePackages = {"com.daou.batch"},
        excludeFilters = {@ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*[Configuration]")}
)

public class TestBatchConfig {

}
