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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user")
@Tag(name = "사용자 정보", description = "사용자 정보를 담당하는 API 그룹")
public class UserController {

	private final TokenRenewService tokenRenewService;

	@PostMapping("/tokenRefresh")
	@Operation(
		summary = "accessToken 재발급",
		description = "refreshToken을 사용하여 새로운 accessToken을 발급합니다.",
		security = {},
		responses = {
			@ApiResponse(responseCode = "200", description = "재발급 성공"),
			@ApiResponse(responseCode = "400", description = "잘못된 요청"),
			@ApiResponse(responseCode = "401", description = "만료된 토큰"),
			@ApiResponse(responseCode = "500", description = "서버 오류")
		}
	)
	public ResTemplate<TokenRefreshResponse> refreshAccessToken(@RequestBody TokenRefreshRequest request) {
		TokenRefreshResponse data = tokenRenewService.renewAccessToken(request);
		return new ResTemplate<>(HttpStatus.OK, "재발급 성공", data);
	}
}
