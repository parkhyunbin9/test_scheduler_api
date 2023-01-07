package com.daou.api.common.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class PerformanceMonitor {

	@Around("execution(* com.daou.api.controller.*.*(..))")
	public Object monitorApiPerformanceTime(ProceedingJoinPoint proceeedingJoinPoint)
		throws Throwable {

		long start = System.currentTimeMillis();
		Object result = proceeedingJoinPoint.proceed();
		long end = System.currentTimeMillis();

		log.info("start = {}, end = {}, processTime = {}ms", start, end, (end - start));

		return result;
	}
}
