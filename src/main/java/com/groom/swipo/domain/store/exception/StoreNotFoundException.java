package com.groom.swipo.domain.store.exception;

import com.groom.swipo.global.error.exception.NotFoundGroupException;

public class StoreNotFoundException extends NotFoundGroupException {
	public StoreNotFoundException(String message) {
		super(message);
	}

	public StoreNotFoundException() {
		this("존재하지 않는 가게입니다.");
	}
}
