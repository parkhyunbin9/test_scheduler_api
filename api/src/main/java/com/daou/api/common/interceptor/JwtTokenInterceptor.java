package com.daou.api.common.interceptor;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.daou.api.common.security.AuthConstant;
import com.daou.api.common.security.JwtTokenUtils;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
		Exception {
		String header = request.getHeader(AuthConstant.AUTH_HEADER);
		if (!Objects.isNull(header)) {
			String token = JwtTokenUtils.getTokenFromHeader(header);
			if (JwtTokenUtils.isValidToken(token)) {
				return true;
			}
		}
		response.sendError(HttpStatus.UNAUTHORIZED_401, "유효하지 않은 토큰입니다.");
		// throw new Error test
		return false;
	}
}
