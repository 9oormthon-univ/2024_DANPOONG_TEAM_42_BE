package com.groom.swipo.domain.user.dto.request;

public record RegisterUserRequest(
	String provider,
	String providerId,
	String name,
	String address,
	String birth,
	String telecom,
	String phone,
	Boolean isMarket,
	String pwd
) {
}
