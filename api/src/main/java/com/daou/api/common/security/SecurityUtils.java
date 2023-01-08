package com.daou.api.common.security;

import java.util.Objects;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.daou.api.common.spec.CommonException;
import com.daou.api.common.spec.ExceptionCode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SecurityUtils {

	private SecurityUtils() {}

	public static Long getCurrentUserId() {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (Objects.isNull(authentication) || Objects.isNull(authentication.getName())) {
			throw new RuntimeException("SecurityContext에 인증 정보가 없습니다.");
		}
		return Long.parseLong(authentication.getName());
	}
}
