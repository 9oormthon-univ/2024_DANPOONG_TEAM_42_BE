package com.groom.swipo.domain.payment.exception;

import com.groom.swipo.global.error.exception.NotFoundGroupException;

public class PayNotFoundException extends NotFoundGroupException {
	public PayNotFoundException(String message) {
		super(message);
	}

	public PayNotFoundException() {
		super("해당 페이를 찾지 못했습니다.");
	}
}
