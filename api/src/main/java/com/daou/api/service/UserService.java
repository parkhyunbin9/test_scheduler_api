package com.daou.api.service;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daou.api.common.spec.CommonException;
import com.daou.api.common.spec.ExceptionCode;
import com.daou.api.dto.request.AuthRequestDto;
import com.daou.api.model.User;
import com.daou.api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserService {

	private final UserRepository userRepository;

	public User findByUsernameAndPassword(final AuthRequestDto authRequestDto) {
		User findUser = userRepository.findByUsernameAndPassword(authRequestDto.getUsername(),
			authRequestDto.getPassword());
		if (Objects.isNull(findUser)) {
			throw new CommonException(ExceptionCode.AUTH_FAIL);
		}
		return findUser;
	}

}
