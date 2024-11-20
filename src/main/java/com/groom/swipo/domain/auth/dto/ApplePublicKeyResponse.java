package com.groom.swipo.domain.auth.dto;

import java.util.List;

import javax.naming.AuthenticationException;

public record ApplePublicKeyResponse(List<ApplePublicKey> keys) {

	public ApplePublicKey getMatchedKey(String kid, String alg) throws AuthenticationException {
		return keys.stream()
			.filter(key -> key.kid().equals(kid) && key.alg().equals(alg))
			.findAny()
			.orElseThrow(AuthenticationException::new);
	}
}