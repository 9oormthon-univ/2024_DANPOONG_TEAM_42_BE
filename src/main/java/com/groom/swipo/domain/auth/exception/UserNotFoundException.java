package com.groom.swipo.domain.auth.exception;

import com.groom.swipo.global.error.exception.NotFoundGroupException;

public class UserNotFoundException extends NotFoundGroupException {
	public UserNotFoundException(String message) {
		super(message);
	}

	public UserNotFoundException() {
		this("존재하지 않는 사용자입니다.");
	}
}
