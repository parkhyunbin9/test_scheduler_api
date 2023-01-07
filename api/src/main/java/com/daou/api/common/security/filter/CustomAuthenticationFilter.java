package com.daou.api.common.security.filter;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.daou.api.common.spec.CommonException;
import com.daou.api.common.spec.ExceptionCode;
import com.daou.api.dto.request.AuthRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	public CustomAuthenticationFilter(final AuthenticationManager authenticationManager) {
		super.setAuthenticationManager(authenticationManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
		HttpServletResponse response) throws AuthenticationException {
		UsernamePasswordAuthenticationToken authReq;
		try {
			AuthRequestDto userInfo = new ObjectMapper().readValue(request.getInputStream(),
				AuthRequestDto.class);
			if (Objects.isNull(userInfo.getUsername()) || Objects.isNull(userInfo.getPassword())) {
				throw new CommonException(ExceptionCode.INVALID_ID_AND_PASSWORD);
			}
			authReq = new UsernamePasswordAuthenticationToken(userInfo.getUsername(),
				userInfo.getPassword());

		} catch (IOException e) {
			log.error("인증 실패 ", e);
			throw new CommonException(ExceptionCode.INVALID_ID_AND_PASSWORD);
		}
		setDetails(request, authReq);
		return this.getAuthenticationManager().authenticate(authReq);
	}
}
