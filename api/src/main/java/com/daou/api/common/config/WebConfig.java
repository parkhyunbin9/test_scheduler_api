package com.daou.api.common.config;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.daou.api.common.filter.IpFilter;
import com.daou.api.common.interceptor.RateLimitInterceptor;
import com.daou.api.common.interceptor.RequestInterceptor;
import com.daou.api.common.manager.IpManager;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	private final IpAddressInterceptor ipAddressInterceptor;
	private final RateLimitInterceptor rateLimitInterceptor;
	private final IpManager ipManager;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {

		registry.addInterceptor(new RequestInterceptor())
			.order(1)
			.addPathPatterns("/api/**");

		registry.addInterceptor(rateLimitInterceptor)
			.order(2)
			.addPathPatterns("/api/**");
	}

	@Bean
	public FilterRegistrationBean IpFilter() {
		FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
		filterFilterRegistrationBean.setFilter(new IpFilter(ipManager));
		filterFilterRegistrationBean.setOrder(1);
		filterFilterRegistrationBean.addUrlPatterns("/*");
		filterFilterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ERROR);
		return filterFilterRegistrationBean;
	}
}

