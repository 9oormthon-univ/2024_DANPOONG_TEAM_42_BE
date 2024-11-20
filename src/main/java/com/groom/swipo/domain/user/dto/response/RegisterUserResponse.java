package com.groom.swipo.domain.user.dto.response;

public record RegisterUserResponse (
	Long userId,
	String accessToken,
	String refreshToken
){
}
