package com.groom.swipo.domain.user.exception;

import com.groom.swipo.global.error.exception.InvalidGroupException;

public class InvalidPasswordException extends InvalidGroupException {

	public InvalidPasswordException(String message) {
		super(message);
	}
	public InvalidPasswordException() {
		this("비밀번호가 일치하지 않습니다.");
	}
}
