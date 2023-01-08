package com.daou.api.service;

import java.util.Objects;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daou.api.common.security.TokenProvider;
import com.daou.api.common.spec.CommonException;
import com.daou.api.common.spec.ExceptionCode;
import com.daou.api.dto.TokenDto;
import com.daou.api.dto.request.TokenRequestDto;
import com.daou.api.dto.request.UserRequestDto;
import com.daou.api.dto.response.UserResponseDto;
import com.daou.api.model.RefreshToken;
import com.daou.api.model.User;
import com.daou.api.repository.RefreshTokenRepository;
import com.daou.api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

	private final UserRepository userRepository;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	private final PasswordEncoder passwordEncoder;
	private final TokenProvider tokenProvider;
	private final RefreshTokenRepository refreshTokenRepository;

	public User findByUsernameAndPassword(final UserRequestDto userRequestDto) {
		User findUser = userRepository.findByUsernameAndPassword(userRequestDto.getUsername(),
			userRequestDto.getPassword());
		if (Objects.isNull(findUser)) {
			throw new CommonException(ExceptionCode.AUTH_FAIL);
		}
		return findUser;
	}

	@Transactional
	public UserResponseDto signUp(UserRequestDto userRequestDto) {
		if (userRepository.existsByUsername(userRequestDto.getUsername())) {
			throw new CommonException(ExceptionCode.ALREADY_REGISTERED_USERS);
		}
		User user = userRequestDto.toUser(passwordEncoder);
		User savedUser = userRepository.save(user);
		return UserResponseDto.from(savedUser);
	}

	@Transactional
	public TokenDto login(UserRequestDto userRequestDto) {
		UsernamePasswordAuthenticationToken authToken = userRequestDto.toAuthentication();
		Authentication authenticate = authenticationManagerBuilder.getObject().authenticate(authToken);
		TokenDto tokenDto = tokenProvider.generateTokenDto(authenticate);

		RefreshToken refreshToken = RefreshToken.builder()
			.key(authenticate.getName())
			.value(tokenDto.getRefreshToken())
			.build();

		refreshTokenRepository.save(refreshToken);

		return tokenDto;
	}

	public TokenDto reissue(TokenRequestDto tokenRequestDto) {
		if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
			throw new CommonException(ExceptionCode.INVALID_TOKEN);
		}
		Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());
		RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
			.orElseThrow(() -> new CommonException(ExceptionCode.LOGOUT_USER));

		if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
			throw new CommonException(ExceptionCode.INVALID_TOKEN);
		}

		TokenDto newToken = tokenProvider.generateTokenDto(authentication);

		RefreshToken newRefreshToken = refreshToken.updateValue(newToken.getRefreshToken());
		refreshTokenRepository.save(newRefreshToken);

		return newToken;
	}


}
