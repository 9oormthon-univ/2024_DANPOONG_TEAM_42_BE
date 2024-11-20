package com.groom.swipo.domain.user.dto.request;

public record PhoneVerificationRequest(
	String phone,
	String code
) {
}