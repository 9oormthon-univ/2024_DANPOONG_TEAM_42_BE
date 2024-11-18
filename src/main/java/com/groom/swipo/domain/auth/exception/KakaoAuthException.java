package com.groom.swipo.domain.auth.exception;

import com.groom.swipo.global.error.exception.AuthGroupException;

public class KakaoAuthException extends AuthGroupException {
	public KakaoAuthException(String message) {
		super(message);
	}

	public KakaoAuthException() {
		this("카카오 서버와의 통신 과정에서 문제가 발생했습니다.");
	}
}
