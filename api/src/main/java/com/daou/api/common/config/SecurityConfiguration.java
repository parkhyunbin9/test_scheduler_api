package com.daou.api.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.daou.api.common.security.JwtAccessDeniedHandler;
import com.daou.api.common.security.JwtAuthenticationEntryPoint;
import com.daou.api.common.security.JwtSecurityConfig;
import com.daou.api.common.security.TokenProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
	private final TokenProvider tokenProvider;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.exceptionHandling()
			.authenticationEntryPoint(jwtAuthenticationEntryPoint)
			.accessDeniedHandler(jwtAccessDeniedHandler)
			.and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.authorizeRequests()
			.antMatchers("/api/user/**").permitAll()
			.antMatchers(SwaggerConfiguration.swaggerPattern).permitAll()
			.anyRequest().authenticated()
			.and()
			.apply(new JwtSecurityConfig(tokenProvider));

		return http.build();
	}

	 @Bean
	 public BCryptPasswordEncoder passwordEncoder() {
	 	return new BCryptPasswordEncoder();
	 }
}
