package com.groom.swipo.domain.user.exception;

import com.groom.swipo.global.error.exception.InvalidGroupException;

public class PhoneVerificationCompletionException extends InvalidGroupException {
	public PhoneVerificationCompletionException(String message) {
		super(message);
	}

	public PhoneVerificationCompletionException() {
		this("인증번호가 일치하지 않습니다.");
	}
}