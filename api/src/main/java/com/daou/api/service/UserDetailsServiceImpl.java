package com.daou.api.service;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.daou.api.common.exception.UserNotFoundException;
import com.daou.api.common.security.MyUserDetails;
import com.daou.api.model.User;
import com.daou.api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public MyUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User findUser = userRepository.findByUsername(username)
			.orElseThrow(() -> new UserNotFoundException(username));

		return new MyUserDetails(findUser,
			Collections.singleton(new SimpleGrantedAuthority(findUser.getRole().getValue())));
	}

}