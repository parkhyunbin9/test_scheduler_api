package com.daou.api.common.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import lombok.extern.slf4j.Slf4j;
import com.daou.api.model.User;

@Slf4j
public class CustomLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws ServletException, IOException {
		User user = ((MyUserDetails)authentication.getPrincipal()).getUser();
		String token = JwtTokenUtils.generateToken(user);
		response.addHeader(AuthConstant.AUTH_HEADER, AuthConstant.TOKEN_TYPE + " " + token);
	}
}
