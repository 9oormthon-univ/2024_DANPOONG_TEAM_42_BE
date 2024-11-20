package com.groom.swipo.domain.user.exception;

import com.groom.swipo.global.error.exception.ConflictGroupException;

public class UserAlreadyExistsException extends ConflictGroupException {

	public UserAlreadyExistsException(String message) {
		super(message);
	}

	public UserAlreadyExistsException() {
		this("이미 해당 소셜로그인으로 가입한 유저가 있습니다.");
	}
}
