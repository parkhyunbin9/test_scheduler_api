package com.daou.api.service;

import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daou.api.common.exception.UserNotFoundException;
import com.daou.api.model.User;
import com.daou.api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findByUsername(username)
			.map(this::createUserDetails)
			.orElseThrow(() -> new UserNotFoundException(username));
	}

	private UserDetails createUserDetails(User user) {
		GrantedAuthority grantAuthority = new SimpleGrantedAuthority(user.getRole().getValue());
		return new org.springframework.security.core.userdetails.User(
			String.valueOf(user.getUsername()),
			user.getPassword(),
			Collections.singleton(grantAuthority)
		);
	}
}