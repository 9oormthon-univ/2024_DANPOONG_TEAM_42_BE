package com.groom.swipo.domain.auth.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import java.security.PublicKey;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

// 해당 코드는 Jwt 전체에 대해 검증하는 것이 아닌 애플 로그인떄 받아오는 itentity token 검증용
@Component
@RequiredArgsConstructor
public class JwtValidator {
	public Map<String, String> parseHeaders(String token) throws JsonProcessingException {
		String header = token.split("\\.")[0];
		return new ObjectMapper().readValue(decodeHeader(header), Map.class);
	}

	public String decodeHeader(String token) {
		return new String(Base64.getDecoder().decode(token), StandardCharsets.UTF_8);
	}

	public Claims getTokenClaims(String token, PublicKey publicKey) {
		return Jwts.parserBuilder()
			.setSigningKey(publicKey)
			.build()
			.parseClaimsJws(token)
			.getBody();
	}
}
