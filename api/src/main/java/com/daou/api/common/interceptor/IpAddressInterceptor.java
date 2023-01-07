package com.daou.api.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.daou.api.common.utils.CustomWebUtils;
import com.daou.api.service.IpService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class IpAddressInterceptor implements HandlerInterceptor {

	private final IpService ipService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
		Exception {
		String ip = CustomWebUtils.getClientIp(request);
		log.info("client ip = {}", ip);
		if (!ipService.isAccessible(ip)) {
			// ipService.unAccessibleIpRequest(ip);
			response.sendError(HttpStatus.FORBIDDEN.value(), "등록되지 않은 IP입니다. 요청 IP : " + ip);
			return false;
		}
		return true;
	}

}
