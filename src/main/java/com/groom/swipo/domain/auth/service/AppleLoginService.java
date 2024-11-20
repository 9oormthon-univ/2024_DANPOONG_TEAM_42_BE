package com.groom.swipo.domain.auth.service;

import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

import javax.naming.AuthenticationException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.groom.swipo.domain.auth.client.AppleAuthClient;
import com.groom.swipo.domain.auth.dto.response.SocialLoginResponse;
import com.groom.swipo.domain.auth.exception.AppleAuthException;
import com.groom.swipo.domain.auth.exception.InvalidTokenException;
import com.groom.swipo.domain.auth.util.ApplePublicKeyGenerator;
import com.groom.swipo.domain.auth.util.JwtValidator;
import com.groom.swipo.domain.user.entity.User;
import com.groom.swipo.domain.user.entity.enums.Provider;
import com.groom.swipo.domain.user.repository.UserRepository;
import com.groom.swipo.global.jwt.TokenProvider;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AppleLoginService {
	private final AppleAuthClient appleAuthClient;
	private final ApplePublicKeyGenerator applePublicKeyGenerator;
	private final JwtValidator jwtValidator;

	private final TokenProvider tokenProvider;
	private final TokenRenewService tokenRenewService;
	private final RestClient restClient;
	private final UserRepository userRepository;

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
	}

	// identity token에서 sub(OAuth 사용자Id) 추출
	public String getAppleAccountId(String identityToken)
		throws JsonProcessingException, AuthenticationException, NoSuchAlgorithmException, InvalidKeySpecException {
		Map<String, String> headers = jwtValidator.parseHeaders(identityToken);
		PublicKey publicKey = applePublicKeyGenerator.generatePublicKey(headers,
			appleAuthClient.getAppleAuthPublicKey());

		return jwtValidator.getTokenClaims(identityToken, publicKey).getSubject();
	}

	// 해당 함수는 중복되기에 따로 authService를 만들어 관리하면 좋을듯..?
	private SocialLoginResponse handleExistingUserLogin(User user) {
		String accessToken = tokenProvider.createAccessToken(user);
		String refreshToken = tokenProvider.createRefreshToken(user);
		tokenRenewService.saveRefreshToken(refreshToken, user.getId());
		return SocialLoginResponse.of(user.getId(), accessToken, refreshToken);
	}
}
