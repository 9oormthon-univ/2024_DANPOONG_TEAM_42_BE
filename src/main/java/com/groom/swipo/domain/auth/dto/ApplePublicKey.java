package com.groom.swipo.domain.auth.dto;

public record ApplePublicKey(
	String kty,
	String kid,
	String alg,
	String n,
	String e) {
}
