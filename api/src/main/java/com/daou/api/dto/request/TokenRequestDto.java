package com.daou.api.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenRequestDto {

	private String accessToken;
	private String refreshToken;

}
