package com.groom.swipo.domain.user.exception;

import com.groom.swipo.global.error.exception.InvalidGroupException;

public class InvalidProviderException extends InvalidGroupException {

	public InvalidProviderException(String message) {
		super(message);
	}

	public InvalidProviderException() {
		this("Provider 형식이 맞지 않습니다.");
	}
}
