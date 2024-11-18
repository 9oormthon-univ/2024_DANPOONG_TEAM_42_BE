package com.groom.swipo.domain.auth.dto.request;

public record TokenRefreshRequest(
	String refreshToken
) {
}
