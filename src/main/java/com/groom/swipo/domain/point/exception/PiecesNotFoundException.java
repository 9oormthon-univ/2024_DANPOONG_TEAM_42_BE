package com.groom.swipo.domain.point.exception;

import com.groom.swipo.global.error.exception.NotFoundGroupException;

public class PiecesNotFoundException extends NotFoundGroupException {
	public PiecesNotFoundException(String message) {
		super(message);
	}
	public PiecesNotFoundException(){
		this("보유한 조각이 존재하지 않습니다.");
	}
}
