package com.groom.swipo.domain.payment.exception;

import com.groom.swipo.global.error.exception.InvalidGroupException;

public class PaymentCompletionException extends InvalidGroupException {
	public PaymentCompletionException(String message) {
		super(message);
	}

	public PaymentCompletionException() {
		this("결제가 완료되지 않았거나 금액이 일치하지 않습니다.");
	}
}
