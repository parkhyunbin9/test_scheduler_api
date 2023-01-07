package com.daou.api.common.spec;

import lombok.Getter;

@Getter
public class CommonException extends RuntimeException {

	private final ExceptionCode exceptionCode;
	private final Object data;

	public CommonException(ExceptionCode exceptionCode) {
		super(exceptionCode.getErrorCode());
		this.exceptionCode = exceptionCode;
		this.data = null;
	}

	public CommonException(ExceptionCode exceptionCode, Object data) {
		super(exceptionCode.getErrorCode());
		this.exceptionCode = exceptionCode;
		this.data = data;
	}

	public CommonErrorCode toErrorCode() {
		return CommonErrorCode.builder()
			.code(exceptionCode.getErrorCode())
			.status(exceptionCode.getErrorStatus())
			.message(exceptionCode.getErrorMessage())
			.build();
	}

}
