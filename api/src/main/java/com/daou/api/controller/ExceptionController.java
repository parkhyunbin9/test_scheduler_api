package com.daou.api.controller;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.TypeMismatchException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import lombok.extern.slf4j.Slf4j;
import com.daou.api.common.spec.CommonErrorCode;
import com.daou.api.common.spec.CommonException;
import com.daou.api.common.spec.CommonResponse;
import com.daou.api.common.spec.ExceptionCode;

@Slf4j
@RestControllerAdvice
public class ExceptionController {

	@ExceptionHandler(CommonException.class)
	public CommonResponse handleCommonException(CommonException e) {
		log.error("내부 정의 에러 ", e.getMessage(), e);

		CommonErrorCode error = e.toErrorCode();

		return CommonResponse
				.builder()
				.success(false)
				.error(error)
				.build();
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public CommonResponse handleIllegalArgumentException (IllegalArgumentException e) {
		log.error(e.getMessage(), e);
		ExceptionCode alreadyRegisteredUsers = ExceptionCode.ALREADY_REGISTERED_USERS;
		return CommonResponse
			.builder()
			.data(alreadyRegisteredUsers.getErrorMessage())
			.error(alreadyRegisteredUsers.getErrorCode())
			.build();
	}

	@ExceptionHandler(value = BadCredentialsException.class)
	public CommonResponse handleBadCredentialException(BadCredentialsException e) {
		log.error(e.getMessage(), e);
		ExceptionCode authFail = ExceptionCode.AUTH_FAIL;

		return CommonResponse.builder()
			.success(false)
			.data(authFail.getErrorMessage())
			.error(authFail.getErrorCode())
			.build();
	}
	@ExceptionHandler(value = ConstraintViolationException.class)
	public CommonResponse handleConstraintViolationException(ConstraintViolationException e) {
		log.error(e.getMessage(), e);
		ExceptionCode invalidRequest = ExceptionCode.INVALID_REQUEST;
		return CommonResponse.builder()
			.success(false)
			.data(invalidRequest.getErrorMessage())
			.error(invalidRequest.getErrorCode())
			.build();
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public CommonResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		log.error(e.getMessage(), e);
		ExceptionCode illegalRequestBody = ExceptionCode.ILLEGAL_REQUEST_BODY;
		return CommonResponse.builder()
			.success(false)
			.data(illegalRequestBody.getErrorMessage())
			.error(illegalRequestBody.getErrorCode())
			.build();
	}

	@ExceptionHandler(ResponseStatusException.class)
	public CommonResponse handleResponseStatusException(ResponseStatusException e) {
		log.error(e.getMessage(), e);
		ExceptionCode notFound = ExceptionCode.NOT_FOUND;
		return CommonResponse.builder()
			.success(false)
			.data(notFound.getErrorMessage())
			.error(notFound.getErrorCode())
			.build();
	}

	@ExceptionHandler(value = BindException.class)
	public CommonResponse handleBindException(BindException e) {

		List<String> errorMessages =
			e.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
				.collect(Collectors.toList());

		return CommonResponse.builder()
			.success(false)
			.data(errorMessages)
			.error(ExceptionCode.INVALID_REQUEST.toString())
			.build();
	}

	@ExceptionHandler(Exception.class)
	public CommonResponse handleDefaultException(Exception e) {

		// exceptionCode에 명시하지 않은 에러 처리
		log.error(e.getMessage(), e);
		log.info("Default EXCEPTION!!!");

		CommonErrorCode error = CommonErrorCode.builder()
			.code(e.getClass().getSimpleName())
			.message(e.getMessage())
			.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.build();

		return CommonResponse
			.builder()
			.success(false)
			.error(error)
			.build();
	}

}
