package com.groom.swipo.domain.user.exception;

import com.groom.swipo.global.error.exception.NotFoundGroupException;

public class PhoneNotFoundException extends NotFoundGroupException {
	public PhoneNotFoundException(String message) {
		super(message);
	}

	public PhoneNotFoundException() {
		this("해당 번호에 인증번호가 가지않았습니다.");
	}
}