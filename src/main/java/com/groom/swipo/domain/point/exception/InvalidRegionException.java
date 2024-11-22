package com.groom.swipo.domain.point.exception;

import com.groom.swipo.global.error.exception.InvalidGroupException;

public class InvalidRegionException extends InvalidGroupException {

	public InvalidRegionException(String message) {
		super(message);
	}
	public InvalidRegionException(){
		this("지역명을 잘못 작성하였거나 유효하지 않는 지역입니다.");
	}
}
