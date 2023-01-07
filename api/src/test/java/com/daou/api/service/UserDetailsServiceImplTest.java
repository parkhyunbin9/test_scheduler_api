package com.daou.api.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.daou.api.common.exception.UserNotFoundException;
import com.daou.api.model.User;
import com.daou.api.model.UserRole;
import com.daou.api.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

	@InjectMocks
	UserDetailsServiceImpl userDetailsService;

	@Mock
	UserRepository userRepository;

	@DisplayName("정상 조회")
	@Test
	void loadUserSuccess() {

		// given
		String testUserName = "test";
		String testPassword = "testPW";
		SimpleGrantedAuthority role_user = new SimpleGrantedAuthority(UserRole.ROLE_USER.getValue());
		Optional<User> expectUser = Optional.of(new User(testUserName, testPassword, UserRole.ROLE_USER));

		when(userRepository.findByUsername(anyString())).thenReturn(expectUser);

		// when
		UserDetails findUser = userDetailsService.loadUserByUsername(testUserName);

		// then
		assertThat(findUser.getUsername()).isEqualTo(testUserName);
		assertThat(findUser.getPassword()).isEqualTo(testPassword);
		assertThat(findUser.getAuthorities().stream()
			.anyMatch(role -> role.getAuthority().equals(UserRole.ROLE_USER.getValue()))).isTrue();
	}

	@DisplayName("권한이 설정되지 않은 유저를 만나면 에러")
	@Test
	void noAuthority() {

		// given
		String testUserName = "test";
		String testPassword = "testPW";
		SimpleGrantedAuthority role_user = new SimpleGrantedAuthority("ROLE_USER");
		Optional<User> expectUser = Optional.of(new User(testUserName, testPassword, null));

		when(userRepository.findByUsername(anyString())).thenReturn(expectUser);

		// when & then
		assertThatThrownBy(() ->
			userDetailsService.loadUserByUsername(testUserName)
		).isInstanceOf(NullPointerException.class);
	}

	@DisplayName("username에 해당하는 유저가 없으면 UserNotFoundException")
	@Test
	void noUser() {

		// given
		String testUserName = "test";
		String testPassword = "testPW";
		SimpleGrantedAuthority role_user = new SimpleGrantedAuthority("ROLE_USER");
		Optional<User> expectUser = Optional.empty();

		when(userRepository.findByUsername(anyString())).thenReturn(expectUser);

		// when & then
		assertThatThrownBy(() ->
			userDetailsService.loadUserByUsername(testUserName)
		).isInstanceOf(UserNotFoundException.class);
	}
}