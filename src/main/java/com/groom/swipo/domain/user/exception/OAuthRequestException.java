package com.groom.swipo.domain.user.exception;

import com.groom.swipo.global.error.exception.InvalidGroupException;

public class OAuthRequestException extends InvalidGroupException {
	public OAuthRequestException(String message) {
		super(message);
	}

	public OAuthRequestException() {
		this("OAuth 정보가 비어있습니다.");
	}
}
