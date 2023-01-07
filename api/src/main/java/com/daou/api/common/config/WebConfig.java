package com.daou.api.common.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;
import com.daou.api.common.interceptor.IpAddressInterceptor;
import com.daou.api.common.interceptor.JwtTokenInterceptor;
import com.daou.api.common.interceptor.RateLimitInterceptor;
import com.daou.api.common.interceptor.RequestInterceptor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {"classpath:/static/",
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

		// 인증 interceptor
		registry.addInterceptor(jwtTokenInterceptor())
			.order(2)
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

	@Bean
	public JwtTokenInterceptor jwtTokenInterceptor() {
		return new JwtTokenInterceptor();
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
	}
}

