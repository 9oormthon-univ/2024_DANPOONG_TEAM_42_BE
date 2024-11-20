package com.groom.swipo.domain.point.exception;

import com.groom.swipo.global.error.exception.InvalidGroupException;

public class InsufficientPointsException extends InvalidGroupException {
	public InsufficientPointsException(String message) {
		super(message);
	}

	public InsufficientPointsException() {
		this("사용 가능한 포인트를 초과하였습니다.");
	}
}
