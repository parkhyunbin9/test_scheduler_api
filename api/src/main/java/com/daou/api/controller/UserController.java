package com.daou.api.controller;

import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daou.api.common.security.JwtTokenUtils;
import com.daou.api.common.spec.CommonResponse;
import com.daou.api.dto.request.AuthRequestDto;
import com.daou.api.model.User;
import com.daou.api.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/user/")
@RequiredArgsConstructor
@Api(value = "저장된 유저 인증 로그인 API")
public class UserController {

	private final UserService userService;

	@ApiOperation(value = "Login")
	@PostMapping(value = "login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public CommonResponse logIn(@Validated @RequestBody AuthRequestDto authRequestDto) {
		User logInUser = userService.findByUsernameAndPassword(authRequestDto);
		return CommonResponse.builder().data(JwtTokenUtils.generateToken(logInUser)).build();
	}

}
