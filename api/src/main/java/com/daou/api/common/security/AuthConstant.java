package com.daou.api.common.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuthConstant {

	public static final String AUTH_HEADER = "Authorization";
	public static final String TOKEN_TYPE = "BEARER";
}
