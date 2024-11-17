package com.groom.swipo.global.jwt;

import java.security.Key;
import java.util.Collections;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.groom.swipo.domain.user.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Component
public class TokenProvider {

	private final Key key;
	private final long accessTokenExpireTime;
	private final long refreshTokenExpireTime;

	public TokenProvider(@Value("${jwt.secret}") String secretKey,
		@Value("${jwt.expire-time.access-token}") long accessTokenExpireTime,
		@Value("${jwt.expire-time.refresh-token}") long refreshTokenExpireTime) {
		this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
		this.accessTokenExpireTime = accessTokenExpireTime;
		this.refreshTokenExpireTime = refreshTokenExpireTime;
	}

	public String createAccessToken(User user) {
		return createToken(user, accessTokenExpireTime);
	}

	public String createRefreshToken(User user) {
		return createToken(user, refreshTokenExpireTime);
	}

	private String createToken(User user, long expireTime) {
		Date now = new Date();
		return Jwts.builder()
			.setSubject(user.getId().toString())
			.setIssuedAt(now)
			.setExpiration(new Date(now.getTime() + expireTime))
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();
	}

	public Long getUserIdFromToken(String token) {
		Claims claims = parseClaims(token);
		return Long.parseLong(claims.getSubject());
	}

	private Claims parseClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	}

	public Authentication getAuthentication(String accessToken) {
		Claims claims = parseClaims(accessToken);
		return new UsernamePasswordAuthenticationToken(claims.getSubject(), "", Collections.emptyList());
	}

	public String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");

		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}

		return null;
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (ExpiredJwtException e) {
			log.error("JWT expired.");
		} catch (SignatureException e) {
			log.error("Invalid JWT signature.");
		} catch (UnsupportedJwtException | MalformedJwtException e) {
			log.error("Invalid JWT format.");
		} catch (IllegalArgumentException e) {
			log.error("JWT is empty or blank.");
		} catch (Exception e) {
			log.error("JWT validation failed.", e);
		}

		return false;
	}
}
