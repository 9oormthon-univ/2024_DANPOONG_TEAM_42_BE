package com.groom.swipo.domain.auth.dto.response;

import lombok.Builder;

@Builder
public record KakaoLoginResponse(
	Long userId,
	String accessToken,
	String refreshToken,
	String providerId,
	String profileImage
) {
	public static KakaoLoginResponse of(Long userId, String accessToken, String refreshToken) {
		return KakaoLoginResponse.builder()
			.userId(userId)
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
	}

	public static KakaoLoginResponse of(String providerId, String profileImage) {
		return KakaoLoginResponse.builder()
			.providerId(providerId)
			.profileImage(profileImage)
			.build();
	}
}
