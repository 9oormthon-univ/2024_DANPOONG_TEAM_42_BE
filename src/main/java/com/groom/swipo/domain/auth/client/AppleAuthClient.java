package com.groom.swipo.domain.auth.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.groom.swipo.domain.auth.dto.ApplePublicKeyResponse;

@FeignClient(name = "appleAuthClient", url = "${oauth.apple.public-key-url}")
public interface AppleAuthClient {
	@GetMapping
	ApplePublicKeyResponse getAppleAuthPublicKey();
}