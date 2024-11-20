package com.groom.swipo.domain.user.exception;

import com.groom.swipo.global.error.exception.InvalidGroupException;

public class InvalidTelecomException extends InvalidGroupException {
	public InvalidTelecomException(String message) {
		super(message);
	}

	public InvalidTelecomException() {
		this("Telecom 형식이 맞지 않습니다.");
	}
}
