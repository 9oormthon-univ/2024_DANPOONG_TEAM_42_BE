package com.groom.swipo.domain.auth.service;

import java.net.URI;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.groom.swipo.domain.auth.dto.response.SocialLoginResponse;
import com.groom.swipo.domain.auth.exception.KakaoAuthException;
import com.groom.swipo.domain.user.entity.User;
import com.groom.swipo.domain.user.entity.enums.Provider;
import com.groom.swipo.domain.user.repository.UserRepository;
import com.groom.swipo.global.jwt.TokenProvider;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class KakaoLoginService {

	private static final String USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";

	private final UserRepository userRepository;
	private final TokenProvider tokenProvider;
	private final TokenRenewService tokenRenewService;
	private final RestClient restClient;
	private final ObjectMapper objectMapper;

	@Transactional
	public SocialLoginResponse kakaoLogin(String kakaoAccessToken) {
		try {
			String[] userInfo = getKakaoUserInfo(kakaoAccessToken);

			return userRepository.findByProviderAndProviderId(Provider.KAKAO, userInfo[0])
				.map(this::handleExistingUserLogin)
				.orElseGet(() -> SocialLoginResponse.of(userInfo[0], userInfo[1]));
		} catch (JsonProcessingException e) {
			throw new KakaoAuthException();
		}
	}

	private SocialLoginResponse handleExistingUserLogin(User user) {
		String accessToken = tokenProvider.createAccessToken(user);
		String refreshToken = tokenProvider.createRefreshToken(user);
		tokenRenewService.saveRefreshToken(refreshToken, user.getId());
		return SocialLoginResponse.of(user.getId(), accessToken, refreshToken);
	}

	private String[] getKakaoUserInfo(String kakaoAccessToken) throws JsonProcessingException {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + kakaoAccessToken);
		headers.setContentType(MediaType.APPLICATION_JSON);

		String response = restClient.get()
			.uri(URI.create(USER_INFO_URL))
			.headers(httpHeaders -> httpHeaders.addAll(headers))
			.retrieve()
			.body(String.class);

		JsonNode jsonNode = objectMapper.readTree(response);
		String providerId = jsonNode.get("id").asText();
		String profileImageUrl = jsonNode.path("kakao_account").path("profile").path("profile_image_url").asText();

		return new String[] {providerId, profileImageUrl};
	}
}
