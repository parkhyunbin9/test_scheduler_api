package com.daou.api.common.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthConst {

	public static final String AUTH_HEADER = "Authorization";
	public static final String AUTH_KEY = "auth";
	public static final String BEARER_TYPE = "BEARER";
	public static final String BEARER_PREFIX = "Bearer ";
}
