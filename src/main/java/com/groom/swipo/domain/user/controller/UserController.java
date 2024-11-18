package com.groom.swipo.domain.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.groom.swipo.domain.auth.dto.request.TokenRefreshRequest;
import com.groom.swipo.domain.auth.dto.response.TokenRefreshResponse;
import com.groom.swipo.domain.auth.service.TokenRenewService;
import com.groom.swipo.global.template.ResTemplate;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
public class UserController {

	private final TokenRenewService tokenRenewService;

	@PostMapping("/tokenRefresh")
	public ResTemplate<TokenRefreshResponse> refreshAccessToken(@RequestBody TokenRefreshRequest request) {
		TokenRefreshResponse data = tokenRenewService.renewAccessToken(request);
		return new ResTemplate<>(HttpStatus.OK, "재발급 성공", data);
	}
}
