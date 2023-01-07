package com.daou.api.common.spec;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@ToString
@Getter
public class CommonResponse {

	private final Object data;
	private final Object error;
	private final HttpStatus code;
	private boolean success = true;

	@Builder
	public CommonResponse(boolean success, Object data, HttpStatus code, Object error) {
		this.success = success;
		this.data = data;
		this.code = code;
		this.error = error;
	}

	public CommonResponse(Object data) {
		this.data = data;
		this.error = null;
		code = null;
	}
}
