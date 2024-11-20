package com.groom.swipo.domain.user.exception;

import com.groom.swipo.global.error.exception.ConflictGroupException;

public class SamePasswordException extends ConflictGroupException {

	public SamePasswordException(String message) {
		super(message);
	}
	public SamePasswordException() {
		this("이전 비밀번호와 동일합니다.");
	}
}
