package com.groom.swipo.domain.auth.service;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.groom.swipo.domain.auth.dto.response.KakaoLoginResponse;
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

	private static final String TOKEN_URL = "https://kauth.kakao.com/oauth/token";
	private static final String USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";

	private final UserRepository userRepository;
	private final TokenProvider tokenProvider;
	private final TokenRenewService tokenRenewService;
	private final RestClient restClient;
	private final ObjectMapper objectMapper;

	@Value("${oauth.kakao.client-id}")
	private String clientId;

	@Value("${oauth.kakao.client-secret}")
	private String clientSecret;

	@Value("${oauth.kakao.redirect-uri}")
	private String redirectUri;

	@Transactional
	public KakaoLoginResponse kakaoLogin(String code) {
		try {
			String kakaoAccessToken = getKakaoAccessToken(code);
			String[] userInfo = getKakaoUserInfo(kakaoAccessToken);

			return userRepository.findByProviderAndProviderId(Provider.KAKAO, userInfo[0])
				.map(this::handleExistingUserLogin)
				.orElseGet(() -> KakaoLoginResponse.of(userInfo[0], userInfo[1]));
		} catch (JsonProcessingException e) {
			throw new KakaoAuthException();
		}
	}

	private KakaoLoginResponse handleExistingUserLogin(User user) {
		String accessToken = tokenProvider.createAccessToken(user);
		String refreshToken = tokenProvider.createRefreshToken(user);
		tokenRenewService.saveRefreshToken(refreshToken, user.getId());
		return KakaoLoginResponse.of(user.getId(), accessToken, refreshToken);
	}

	private String getKakaoAccessToken(String code) throws JsonProcessingException {
		String url = UriComponentsBuilder.fromHttpUrl(TOKEN_URL)
			.queryParam("grant_type", "authorization_code")
			.queryParam("client_id", clientId)
			.queryParam("redirect_uri", redirectUri)
			.queryParam("code", code)
			.queryParam("client_secret", clientSecret)
			.toUriString();

		String response = restClient.post()
			.uri(url)
			.contentType(MediaType.APPLICATION_FORM_URLENCODED)
			.retrieve()
			.body(String.class);

		JsonNode jsonNode = objectMapper.readTree(response);
		return jsonNode.get("access_token").asText();
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
