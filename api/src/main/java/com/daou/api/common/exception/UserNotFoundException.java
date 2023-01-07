package com.daou.api.common.exception;

public class UserNotFoundException extends RuntimeException {

	public UserNotFoundException(String username) {
		super(username + " NotFoundException");
	}
}
