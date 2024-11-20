package com.groom.swipo.domain.point.exception;

import com.groom.swipo.global.error.exception.NotFoundGroupException;

public class CardNotFoundException extends NotFoundGroupException {
	public CardNotFoundException(String message) {
		super(message);
	}

	public CardNotFoundException() {
		this("존재하지 않는 카드입니다.");
	}
}
