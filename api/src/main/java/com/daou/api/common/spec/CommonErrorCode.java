package com.daou.api.common.spec;

import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommonErrorCode {

	private final String errorCode;
	private final String errorMessage;
	private final HttpStatus errorStatus;

	@Builder
	CommonErrorCode(String code, String message, HttpStatus status) {
		errorCode = code;
		errorMessage = message;
		errorStatus = status;
	}
}
