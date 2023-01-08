package com.daou.api.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.daou.api.common.interceptor.IpAddressInterceptor;
import com.daou.api.common.interceptor.RateLimitInterceptor;
import com.daou.api.common.interceptor.RequestInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	public static final String[] CLASSPATH_RESOURCE_LOCATIONS = {"classpath:/static/",
		"classpath:/public/", "classpath:/resources/", "classpath:/META-INF/resources/webjars/",
		"classpath:/META-INF/resources/", "classpath:/"};
	private final IpAddressInterceptor ipAddressInterceptor;
	private final RateLimitInterceptor rateLimitInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {

		// log interceptor
		registry.addInterceptor(new RequestInterceptor())
			.order(1)
			.addPathPatterns("/api/**");

		// ip whiteList interceptor
		registry.addInterceptor(ipAddressInterceptor)
			.order(3)
			.addPathPatterns("/api/hello");
		// api-limit interceptor
		registry.addInterceptor(rateLimitInterceptor)
			.order(4)
			.addPathPatterns("/api/hello");
	}


	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
	}
}

