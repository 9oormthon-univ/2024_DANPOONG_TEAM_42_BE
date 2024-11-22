package com.groom.swipo.domain.point.exception;

import com.groom.swipo.global.error.exception.ConflictGroupException;

public class DuplicateCardException extends ConflictGroupException {
	public DuplicateCardException(String message) {
		super(message);
	}
	public DuplicateCardException(){
		this("이미 해당 지역카드가 존재합니다.");
	}
}
