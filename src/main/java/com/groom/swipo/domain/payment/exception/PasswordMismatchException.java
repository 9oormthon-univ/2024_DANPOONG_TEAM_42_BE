package com.groom.swipo.domain.payment.exception;

import com.groom.swipo.global.error.exception.InvalidGroupException;

public class PasswordMismatchException extends InvalidGroupException {
	public PasswordMismatchException(String message) {
		super(message);
	}

	public PasswordMismatchException() {
		this("사용자 패스워드가 일치하지 않습니다.");
	}
}
