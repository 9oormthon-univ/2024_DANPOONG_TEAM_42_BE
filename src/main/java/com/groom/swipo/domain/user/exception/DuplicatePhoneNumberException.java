package com.groom.swipo.domain.user.exception;

import com.groom.swipo.global.error.exception.ConflictGroupException;

public class DuplicatePhoneNumberException extends ConflictGroupException {

	public DuplicatePhoneNumberException(String message) {
		super(message);
	}
	public DuplicatePhoneNumberException(){
		this("이미 존재하는 휴대폰 번호입니다.");
	}
}
