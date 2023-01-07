package com.daou.api.common.spec;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionCode {
	// Common
	INVALID_REQUEST("InvalidRequest", "입력값 유효성 검사 실패", HttpStatus.BAD_REQUEST),
	COMMON_ERROR("CommonError", "일반적인 오류", HttpStatus.INTERNAL_SERVER_ERROR),
	NOT_FOUND("ResourceNotFound", "요청 데이터 없음", HttpStatus.NOT_FOUND),

	// REQUEST
	INVALID_ID_AND_PASSWORD("InvalidIdAndPW", "ID와 패스워드가 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
	INVALID_REQUEST_IP("InvalidRequestIp", "허가되지 않은 IP 주소 입니다.", HttpStatus.FORBIDDEN),
	TOO_MANY_REQUEST("TooManyRequest", "요청 횟수 초과", HttpStatus.TOO_MANY_REQUESTS),
	ILLEGAL_REQUEST_BODY("IllegamRequestBody", "잘못된 입력값", HttpStatus.BAD_REQUEST),
	AUTH_FAIL("AuthFail", "인증 실패", HttpStatus.BAD_REQUEST),
	NO_PERMISSION("NoPermission", "접근 권한이 없음", HttpStatus.FORBIDDEN),

	ALREADY_REGISTERED_USERS("AlreadyRegisteredUsers", "이미 등록된 유저", HttpStatus.BAD_REQUEST),
	ALREADY_AGGREGATED_DATA("AlreadyAggregateData", "이미 집계된 데이터", HttpStatus.BAD_REQUEST);

	private final String errorCode;
	private final String errorMessage;
	private final HttpStatus errorStatus;

	@Override
	public String toString() {
		return errorStatus + " (" + errorMessage + ")";
	}
}