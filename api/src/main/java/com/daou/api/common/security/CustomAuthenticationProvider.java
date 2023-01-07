package com.daou.api.common.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

	private final UserDetailsService userDetailsService;
	private final BCryptPasswordEncoder passwordEncoder;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		final UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken)authentication;
		final String username = token.getName();
		final String password = (String)token.getCredentials();
		final MyUserDetails userDetails = (MyUserDetails)userDetailsService.loadUserByUsername(username);

		if (!passwordEncoder.matches(password, userDetails.getPassword())) {
			throw new BadCredentialsException(userDetails.getUsername() + " Invalid Password");
		}
		return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}
