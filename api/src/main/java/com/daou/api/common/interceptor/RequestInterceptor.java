package com.daou.api.common.interceptor;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RequestInterceptor implements HandlerInterceptor {

	private static final String LOG_ID = "API_REQUEST_ID";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
		Exception {
		String requestURI = request.getRequestURI();
		String uuid = UUID.randomUUID().toString();

		request.setAttribute(LOG_ID, uuid);

		// @RequestMapping -> handleMethod
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod)handler; // 호출할 컨트롤러 메서드의 정보
		}
		log.info("REQUEST [{}] [{}] [{}] [{}] ", uuid, request.getDispatcherType(), requestURI,
			handler);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
		ModelAndView modelAndView) throws Exception {
		log.info("POST HANDLE {}", modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
		Exception ex) throws Exception {
		String requestURI = request.getRequestURI();
		Object requestId = request.getAttribute(LOG_ID);

		log.info("RESPONSE [{}] [{}] [{}]", requestId, request.getDispatcherType(), handler);
		if (ex != null) {
			log.error("afterCompletion Error!!", ex);
		}
	}
}
