package com.daou.api.common.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.daou.api.common.security.filter.CustomAuthenticationFilter;

public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {

	public static MyCustomDsl customDsl() {
		return new MyCustomDsl();
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		AuthenticationManager authenticationManager = http.getSharedObject(
			AuthenticationManager.class);
		CustomAuthenticationFilter customFilter = new CustomAuthenticationFilter(
			authenticationManager);
		customFilter.setFilterProcessesUrl("/api/user/login");
		customFilter.setAuthenticationSuccessHandler(customLoginSuccessHandler());
		customFilter.afterPropertiesSet();
		http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
	}

	@Bean
	public CustomLoginSuccessHandler customLoginSuccessHandler() {
		return new CustomLoginSuccessHandler();
	}

}
