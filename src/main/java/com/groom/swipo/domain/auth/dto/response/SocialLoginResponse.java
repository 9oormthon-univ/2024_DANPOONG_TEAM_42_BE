package com.groom.swipo.domain.auth.dto.response;

import lombok.Builder;

@Builder
public record SocialLoginResponse(
	Long userId,
	String accessToken,
	String refreshToken,
	String providerId,
	String profileImage
) {
	public static SocialLoginResponse of(Long userId, String accessToken, String refreshToken) {
		return SocialLoginResponse.builder()
			.userId(userId)
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
	}

	public static SocialLoginResponse of(String providerId, String profileImage) {
		return SocialLoginResponse.builder()
			.providerId(providerId)
			.profileImage(profileImage)
			.build();
	}
}
