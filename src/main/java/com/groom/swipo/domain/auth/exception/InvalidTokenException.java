package com.groom.swipo.domain.auth.exception;

import com.groom.swipo.global.error.exception.AuthGroupException;

public class InvalidTokenException extends AuthGroupException {
	public InvalidTokenException(String message) {
		super(message);
	}

	public static InvalidTokenException expired() {
		return new InvalidTokenException("토큰이 만료되었습니다.");
	}

	public static InvalidTokenException invalidSignature() {
		return new InvalidTokenException("유효하지 않은 서명입니다.");
	}

	public static InvalidTokenException invalidToken() {
		return new InvalidTokenException("유효하지 않은 토큰입니다.");
	}
}
