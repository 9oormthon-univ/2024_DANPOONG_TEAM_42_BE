package com.groom.swipo.domain.auth.exception;

import com.groom.swipo.global.error.exception.AuthGroupException;

public class AppleAuthException extends AuthGroupException {
	public AppleAuthException(String message) {
		super(message);
	}
	public AppleAuthException() {
		this("애플 서버와의 통신 과정에서 문제가 발생했습니다.");
	}
}