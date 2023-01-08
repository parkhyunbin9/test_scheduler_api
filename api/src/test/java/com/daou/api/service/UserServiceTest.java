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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.daou.api.common.security.AuthConst;
import com.daou.api.common.security.TokenProvider;
import com.daou.api.common.spec.CommonException;
import com.daou.api.common.spec.ExceptionCode;
import com.daou.api.dto.TokenDto;
import com.daou.api.dto.request.UserRequestDto;
import com.daou.api.dto.response.UserResponseDto;
import com.daou.api.model.RefreshToken;
import com.daou.api.model.User;
import com.daou.api.repository.RefreshTokenRepository;
import com.daou.api.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@InjectMocks
	UserService userService;

	@Mock
	UserRepository userRepository;
	@Mock
	TokenProvider tokenProvider;
	@Mock
	AuthenticationManagerBuilder authenticationManagerBuilder;
	@Autowired
	RefreshTokenRepository refreshTokenRepository;
	@Autowired
	PasswordEncoder passwordEncoder;

	@DisplayName("인증 정보로 유저 정보 가져온다")
	@Test
	void findUserByUsernameAndPassword() {

		// given
		UserRequestDto testData = new UserRequestDto("testUser", "testPassword");
		User expectUser = new User(testData.getUsername(), testData.getPassword());
		when(userRepository.findByUsernameAndPassword(testData.getUsername(), testData.getPassword())).thenReturn(
			expectUser);

		// when
		User findUser = userService.findByUsernameAndPassword(testData);

		//then
		assertThat(findUser).isEqualTo(expectUser);
	}

	@DisplayName("인증 정보에 해당하는 유저가 없을때 Auth Fail")
	@Test
	void noUser() {

		// given
		UserRequestDto testData = new UserRequestDto("noUser", "noUser");
		User expectUser = null;
		when(userRepository.findByUsernameAndPassword(testData.getUsername(), testData.getPassword())).thenReturn(
			expectUser);

		//then
		assertThatThrownBy(() -> userService.findByUsernameAndPassword(testData))
			.isInstanceOf(CommonException.class);
		assertThatThrownBy(() -> userService.findByUsernameAndPassword(testData))
			.hasMessage(ExceptionCode.AUTH_FAIL.getErrorCode());
	}

}