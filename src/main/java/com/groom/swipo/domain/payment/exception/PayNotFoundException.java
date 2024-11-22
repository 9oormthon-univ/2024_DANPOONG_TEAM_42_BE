package com.groom.swipo.domain.payment.exception;

import com.groom.swipo.global.error.exception.NotFoundGroupException;

public class PayNotFoundException extends NotFoundGroupException {
	public PayNotFoundException(String message) {
		super(message);
	}

	public PayNotFoundException() {
		super("Pay not found for user.");
	}
}
