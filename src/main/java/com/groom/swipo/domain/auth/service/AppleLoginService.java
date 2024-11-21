package com.groom.swipo.domain.auth.service;

import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;
import javax.naming.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.groom.swipo.domain.auth.dto.response.ApplePublicKeyResponse;
import com.groom.swipo.domain.auth.dto.response.SocialLoginResponse;
import com.groom.swipo.domain.auth.exception.AppleAuthException;
import com.groom.swipo.domain.auth.exception.InvalidTokenException;
import com.groom.swipo.domain.auth.util.ApplePublicKeyGenerator;
import com.groom.swipo.domain.auth.util.JwtValidator;
import com.groom.swipo.domain.user.entity.User;
import com.groom.swipo.domain.user.entity.enums.Provider;
import com.groom.swipo.domain.user.repository.UserRepository;
import com.groom.swipo.global.jwt.TokenProvider;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AppleLoginService {

	private static final String APPLE_PUBLIC_KEY_URL = "https://appleid.apple.com/auth/keys";

	private final ApplePublicKeyGenerator applePublicKeyGenerator;
	private final JwtValidator jwtValidator;
	private final TokenProvider tokenProvider;
	private final TokenRenewService tokenRenewService;
	private final RestClient restClient;
	private final UserRepository userRepository;
	private final ObjectMapper objectMapper;

	@Transactional
	public SocialLoginResponse appleLogin(String code) {
		try {
			String appleAccountId = getAppleAccountId(code);
			return userRepository.findByProviderAndProviderId(Provider.APPLE, appleAccountId)
				.map(this::handleExistingUserLogin)
				.orElseGet(() -> SocialLoginResponse.of(appleAccountId, "default 이미지입니다."));
		} catch (JsonProcessingException e) {
			throw new InvalidTokenException("JSON 형식이 잘못되었습니다.");
		} catch (AuthenticationException e) {
			throw new AppleAuthException("인증 실패");
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new InvalidTokenException("유효하지 않는 identity Token 입니다.");
		}
		catch (IllegalArgumentException e) {
			throw new InvalidTokenException("토큰이 만료되었거나 잘못된 인자가 포함되어있습니다.");
		}
	}

	private SocialLoginResponse handleExistingUserLogin(User user) {
		String accessToken = tokenProvider.createAccessToken(user);
		String refreshToken = tokenProvider.createRefreshToken(user);
		tokenRenewService.saveRefreshToken(refreshToken, user.getId());
		return SocialLoginResponse.of(user.getId(), accessToken, refreshToken);
	}

	private String getAppleAccountId(String identityToken)
		throws JsonProcessingException, AuthenticationException, NoSuchAlgorithmException, InvalidKeySpecException {
		if (identityToken == null) {
			throw new IllegalArgumentException("토큰이 비어있습니다.");
		}

		Map<String, String> headers = jwtValidator.parseHeaders(identityToken);
		ApplePublicKeyResponse applePublicKeys = getAppleAuthPublicKey();
		PublicKey publicKey = applePublicKeyGenerator.generatePublicKey(headers, applePublicKeys);

		try {
			return jwtValidator.getTokenClaims(identityToken, publicKey).getSubject();
		} catch (ExpiredJwtException e) {
			throw new IllegalArgumentException("토큰이 만료되었습니다.", e);
		}
	}

	private ApplePublicKeyResponse getAppleAuthPublicKey() throws JsonProcessingException {
		String response = restClient.get()
			.uri(URI.create(APPLE_PUBLIC_KEY_URL))
			.retrieve()
			.body(String.class);

		return objectMapper.readValue(response, ApplePublicKeyResponse.class);
	}
}