package com.daou.api.controller;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindException;

import com.daou.api.common.spec.CommonException;
import com.daou.api.common.spec.ExceptionCode;
import com.daou.api.dto.TokenDto;
import com.daou.api.dto.request.UserRequestDto;
import com.daou.api.model.User;
import com.daou.api.model.UserRole;
import com.daou.api.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

	@InjectMocks
	UserController userController;

	@Mock
	UserService userService;

	ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

	private MockMvc mock;

	@BeforeEach
	public void init() {
		mock = MockMvcBuilders.standaloneSetup(userController)
			.build();
	}



	@DisplayName("필수값 누락 - username")
	@Test
	void withoutUsernameFail() throws Exception {

		UserRequestDto userRequestDto = new UserRequestDto("", "password");
		String userStr = objectMapper.writeValueAsString(userRequestDto);
		User user = new User(userRequestDto.getUsername(), userRequestDto.getPassword());

		lenient().when(userService.findByUsernameAndPassword(any(UserRequestDto.class))).thenReturn(user);

		mock.perform(
				post("/api/user/login")
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(userStr)
					.characterEncoding(StandardCharsets.UTF_8)
			).andDo(print())
			.andExpect(MockMvcResultMatchers
				.jsonPath("$.data").doesNotExist())
			.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
			.andExpect(status().isBadRequest());
	}

	@DisplayName("필수값 누락 - password")
	@Test
	void withoutPasswordFail() throws Exception {

		UserRequestDto userRequestDto = new UserRequestDto("username", "");
		String userStr = objectMapper.writeValueAsString(userRequestDto);
		User user = new User(userRequestDto.getUsername(), userRequestDto.getPassword());

		lenient().when(userService.findByUsernameAndPassword(any(UserRequestDto.class))).thenReturn(user);

		mock.perform(
				post("/api/user/login")
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(userStr)
					.characterEncoding(StandardCharsets.UTF_8)
			).andDo(print())
			.andExpect(MockMvcResultMatchers
				.jsonPath("$.data").doesNotExist())
			.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class))
			.andExpect(status().isBadRequest());
	}


}