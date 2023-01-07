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
import com.daou.api.dto.request.AuthRequestDto;
import com.daou.api.model.User;
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

	@DisplayName("인증")
	@Test
	void logInSuccess() throws Exception {

		AuthRequestDto authRequestDto = new AuthRequestDto("user", "userPassword");
		String userStr = objectMapper.writeValueAsString(authRequestDto);

		when(userService.findByUsernameAndPassword(any(AuthRequestDto.class))).thenReturn(
			new User(authRequestDto.getUsername(), authRequestDto.getPassword()));

		mock.perform(
				post("/api/user/login")
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(userStr)
					.characterEncoding(StandardCharsets.UTF_8)
			).andDo(print())
			.andExpect(MockMvcResultMatchers
				.jsonPath("$.data").exists())
			.andExpect(status().isOk());
	}

	@DisplayName("잘못된 username - duplicated")
	@Test
	void wrongUserName() throws Exception {

		AuthRequestDto authRequestDto = new AuthRequestDto("user","password");
		String userStr = objectMapper.writeValueAsString(authRequestDto);
		User user = new User(authRequestDto.getUsername(), authRequestDto.getPassword());

		lenient().when(userService.findByUsernameAndPassword(any(AuthRequestDto.class))).thenThrow(new CommonException(ExceptionCode.NOT_FOUND));
		assertThatThrownBy(() ->
			mock.perform(
				post("/api/user/login")
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(userStr)
					.characterEncoding(StandardCharsets.UTF_8)
			).andDo(print())).hasCause(new CommonException(ExceptionCode.NOT_FOUND, ""));

	}

	@DisplayName("필수값 누락 - username")
	@Test
	void withoutUsernameFail() throws Exception {

		AuthRequestDto authRequestDto = new AuthRequestDto("","password");
		String userStr = objectMapper.writeValueAsString(authRequestDto);
		User user = new User(authRequestDto.getUsername(), authRequestDto.getPassword());

		lenient().when(userService.findByUsernameAndPassword(any(AuthRequestDto.class))).thenReturn(user);

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

		AuthRequestDto authRequestDto = new AuthRequestDto("username","");
		String userStr = objectMapper.writeValueAsString(authRequestDto);
		User user = new User(authRequestDto.getUsername(), authRequestDto.getPassword());

		lenient().when(userService.findByUsernameAndPassword(any(AuthRequestDto.class))).thenReturn(user);

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

	@DisplayName("json타입만 처리할 수 있다.")
	@Test
	void requestConsumeJSON() throws Exception {

		AuthRequestDto authRequestDto = new AuthRequestDto("user", "userPassword");
		String userStr = objectMapper.writeValueAsString(authRequestDto);

		when(userService.findByUsernameAndPassword(any(AuthRequestDto.class))).thenReturn(
			new User(authRequestDto.getUsername(), authRequestDto.getPassword()));

		mock.perform(
				post("/api/user/login")
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(userStr)
					.characterEncoding(StandardCharsets.UTF_8)
			).andDo(print())
			.andExpect(MockMvcResultMatchers
				.jsonPath("$.data").exists())
			.andExpect(status().isOk());

		mock.perform(
				post("/api/user/login")
					.contentType(MediaType.ALL_VALUE)
					.content(userStr)
					.characterEncoding(StandardCharsets.UTF_8)
			).andDo(print())
			.andExpect(MockMvcResultMatchers
				.jsonPath("$.data").doesNotExist())
			.andExpect(status().isUnsupportedMediaType());

		mock.perform(
				post("/api/user/login")
					.contentType(MediaType.APPLICATION_XML)
					.content(userStr)
					.characterEncoding(StandardCharsets.UTF_8)
			).andDo(print())
			.andExpect(MockMvcResultMatchers
				.jsonPath("$.data").doesNotExist())
			.andExpect(status().isUnsupportedMediaType());

		mock.perform(
				post("/api/user/login")
					.contentType(MediaType.TEXT_HTML_VALUE)
					.content(userStr)
					.characterEncoding(StandardCharsets.UTF_8)
			).andDo(print())
			.andExpect(MockMvcResultMatchers
				.jsonPath("$.data").doesNotExist())
			.andExpect(status().isUnsupportedMediaType());

		mock.perform(
				post("/api/user/login")
					.contentType(MediaType.MULTIPART_FORM_DATA)
					.content(userStr)
					.characterEncoding(StandardCharsets.UTF_8)
			).andDo(print())
			.andExpect(MockMvcResultMatchers
				.jsonPath("$.data").doesNotExist())
			.andExpect(status().isUnsupportedMediaType());
	}

	@DisplayName("json으로 return 한다.")
	@Test
	void requestProduceJSON() throws Exception {

		AuthRequestDto authRequestDto = new AuthRequestDto("user", "userPassword");
		String userStr = objectMapper.writeValueAsString(authRequestDto);

		when(userService.findByUsernameAndPassword(any(AuthRequestDto.class))).thenReturn(
			new User(authRequestDto.getUsername(), authRequestDto.getPassword()));

		// when
		String loginResponseContentType =
			mock.perform(
				post("/api/user/login")
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(userStr)
					.characterEncoding(StandardCharsets.UTF_8)
			).andReturn().getResponse().getContentType();


		// then
		assertThat(loginResponseContentType).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
	}



}