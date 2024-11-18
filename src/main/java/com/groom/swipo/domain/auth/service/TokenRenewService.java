package com.groom.swipo.domain.auth.service;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.groom.swipo.domain.auth.dto.request.TokenRefreshRequest;
import com.groom.swipo.domain.auth.dto.response.TokenRefreshResponse;
import com.groom.swipo.domain.auth.exception.InvalidTokenException;
import com.groom.swipo.domain.auth.exception.UserNotFoundException;
import com.groom.swipo.domain.user.entity.User;
import com.groom.swipo.domain.user.repository.UserRepository;
import com.groom.swipo.global.jwt.TokenProvider;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TokenRenewService {

	private static final String REFRESH_TOKEN_PREFIX = "refreshToken:";

	private final RedisTemplate<String, String> redisTemplate;
	private final TokenProvider tokenProvider;
	private final UserRepository userRepository;

	@Transactional
	public TokenRefreshResponse renewAccessToken(TokenRefreshRequest request) {
		Long userId = tokenProvider.getUserIdFromToken(request.refreshToken());
		String storedRefreshToken = redisTemplate.opsForValue().get(REFRESH_TOKEN_PREFIX + userId);

		if (storedRefreshToken == null || !storedRefreshToken.equals(request.refreshToken())) {
			throw InvalidTokenException.invalidToken();
		}

		User user = userRepository.findById(userId)
			.orElseThrow(UserNotFoundException::new);
		String newAccessToken = tokenProvider.createAccessToken(user);

		return new TokenRefreshResponse(newAccessToken);
	}

	@Transactional
	public void saveRefreshToken(String refreshToken, Long userId) {
		redisTemplate.opsForValue()
			.set(REFRESH_TOKEN_PREFIX + userId, refreshToken, tokenProvider.getRefreshTokenExpireTime(),
				TimeUnit.MILLISECONDS);
	}
}
