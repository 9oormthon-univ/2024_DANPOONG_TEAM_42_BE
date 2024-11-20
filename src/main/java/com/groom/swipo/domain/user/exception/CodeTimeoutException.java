package com.groom.swipo.domain.user.exception;

import com.groom.swipo.global.error.exception.TimeoutGroupException;

public class CodeTimeoutException extends TimeoutGroupException {
	public CodeTimeoutException(String message) {
		super(message);
	}

	public CodeTimeoutException() {
		this("인증 시간이 만료되었습니다.");
	}
}