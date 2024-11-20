package com.groom.swipo.domain.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.groom.swipo.domain.auth.dto.request.SocialLoginRequest;
import com.groom.swipo.domain.auth.dto.request.TokenRefreshRequest;
import com.groom.swipo.domain.auth.dto.response.SocialLoginResponse;
import com.groom.swipo.domain.auth.dto.response.TokenRefreshResponse;
import com.groom.swipo.domain.auth.service.AppleLoginService;
import com.groom.swipo.domain.auth.service.KakaoLoginService;
import com.groom.swipo.domain.auth.service.TokenRenewService;
import com.groom.swipo.global.template.ResTemplate;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user")
@Tag(name = "사용자", description = "사용자를 담당하는 API 그룹")
public class UserController {

	private final KakaoLoginService kakaoLoginService;
	private final TokenRenewService tokenRenewService;
	private final AppleLoginService appleLoginService;

	@PostMapping("/tokenRefresh")
	@Operation(
		summary = "액세스 토큰 재발급",
		description = "리프레쉬 토큰을 사용하여 새로운 액세스 토큰을 발급합니다.",
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

	@PostMapping("/kakao")
	@Operation(
		summary = "카카오 로그인",
		description = "카카오 인가 코드를 사용하여 로그인 또는 회원가입 필요 여부를 판별합니다. DB에 사용자 정보가 있으면 로그인 성공, 없으면 회원가입 필요 상태를 반환합니다.",
		security = {},
		responses = {
			@ApiResponse(responseCode = "200", description = "로그인 성공"),
			@ApiResponse(responseCode = "400", description = "잘못된 요청"),
			@ApiResponse(responseCode = "401", description = "유효하지 않은 인가 코드"),
			@ApiResponse(responseCode = "418", description = "회원가입 필요"),
			@ApiResponse(responseCode = "500", description = "서버 오류")
		}
	)
	public ResTemplate<SocialLoginResponse> kakaoLogin(@RequestBody SocialLoginRequest request) {
		SocialLoginResponse data = kakaoLoginService.kakaoLogin(request.code());
		if (data.userId() == null) {
			return new ResTemplate<>(HttpStatus.I_AM_A_TEAPOT, "회원가입 필요", data);
		}
		return new ResTemplate<>(HttpStatus.OK, "로그인 성공", data);
	}

	// 애플 로그인
	@PostMapping("/apple")
	@Operation(
		summary = "애플 로그인",
		description = "애플 인가 코드를 사용하여 로그인 또는 회원가입 필요 여부를 판별합니다. DB에 사용자 정보가 있으면 로그인 성공, 없으면 회원가입 필요 상태를 반환합니다.",
		security = {},
		responses = {
			@ApiResponse(responseCode = "200", description = "로그인 성공"),
			@ApiResponse(responseCode = "400", description = "잘못된 요청"),
			@ApiResponse(responseCode = "401", description = "유효하지 않은 Identity Token 입니다"),
			@ApiResponse(responseCode = "418", description = "회원가입 필요"),
			@ApiResponse(responseCode = "500", description = "서버 오류")
		}
	)
	public ResTemplate<SocialLoginResponse> appleLogin(@RequestBody SocialLoginRequest request) {
		SocialLoginResponse data = appleLoginService.appleLogin(request.code());
		if (data.userId() == null) {
			return new ResTemplate<>(HttpStatus.I_AM_A_TEAPOT, "회원가입 필요", data);
		}
		return new ResTemplate<>(HttpStatus.OK, "로그인 성공", data);
	}

	// 문자인증
	// 문자인증 검증
	// 회원가입
	// 비밀번호 변경
	// 회원탈퇴
	// 마이페이지 조회
	// 휴대폰번호 변경

}
