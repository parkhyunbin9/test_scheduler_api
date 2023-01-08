package com.daou.api.dto.response;

import com.daou.api.model.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {

	private String username;

	public static UserResponseDto from(User uesr) {
		return new UserResponseDto(uesr.getUsername());
	}
}
