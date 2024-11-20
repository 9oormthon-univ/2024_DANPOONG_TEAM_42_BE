package com.groom.swipo.domain.user.controller;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.groom.swipo.domain.auth.dto.request.KakaoLoginRequest;
import com.groom.swipo.domain.auth.dto.request.TokenRefreshRequest;
import com.groom.swipo.domain.auth.dto.response.KakaoLoginResponse;
import com.groom.swipo.domain.auth.dto.response.TokenRefreshResponse;
import com.groom.swipo.domain.auth.service.KakaoLoginService;
import com.groom.swipo.domain.auth.service.TokenRenewService;
import com.groom.swipo.domain.user.dto.request.ChkPwdRequest;
import com.groom.swipo.domain.user.dto.request.RegisterUserRequest;
import com.groom.swipo.domain.user.dto.response.RegisterUserResponse;
import com.groom.swipo.domain.user.service.UserService;
import com.groom.swipo.global.template.ResTemplate;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user")
@Tag(name = "사용자", description = "사용자 관련 API 그룹")
public class UserController {
	private final UserService userService;
	private final KakaoLoginService kakaoLoginService;
	private final TokenRenewService tokenRenewService;

	@PostMapping("/kakao")
	@Operation(
		summary = "카카오 로그인",
		description = "카카오 인가 코드를 사용하여 로그인 또는 회원가입 필요 여부를 판별합니다. DB에 사용자 정보가 있으면 로그인 성공, 없으면 회원가입 필요 상태를 반환합니다.",
		security = {},
		responses = {
			@ApiResponse(responseCode = "200", description = "로그인 성공"),
			@ApiResponse(responseCode = "400", description = "잘못된 요청"),
			@ApiResponse(responseCode = "401", description = "인증되지 않은 요청"),
			@ApiResponse(responseCode = "418", description = "회원가입 필요"),
			@ApiResponse(responseCode = "500", description = "서버 오류")
		}
	)
	public ResTemplate<KakaoLoginResponse> kakaoLogin(@RequestBody KakaoLoginRequest request) {
		KakaoLoginResponse data = kakaoLoginService.kakaoLogin(request.kakaoCode());
		if (data.userId() == null) {
			return new ResTemplate<>(HttpStatus.I_AM_A_TEAPOT, "회원가입 필요", data);
		}
		return new ResTemplate<>(HttpStatus.OK, "로그인 성공", data);
	}

	@PostMapping("/tokenRefresh")
	@Operation(
		summary = "액세스 토큰 재발급",
		description = "리프레쉬 토큰을 사용하여 새로운 액세스 토큰을 발급합니다.",
		security = {},
		responses = {
			@ApiResponse(responseCode = "200", description = "재발급 성공"),
			@ApiResponse(responseCode = "400", description = "잘못된 요청"),
			@ApiResponse(responseCode = "401", description = "인증되지 않은 요청"),
			@ApiResponse(responseCode = "500", description = "서버 오류")
		}
	)
	public ResTemplate<TokenRefreshResponse> refreshAccessToken(@RequestBody TokenRefreshRequest request) {
		TokenRefreshResponse data = tokenRenewService.renewAccessToken(request);
		return new ResTemplate<>(HttpStatus.OK, "재발급 성공", data);
	}

	// 문자인증
	// 문자인증 검증

	// 회원가입
	@PostMapping("/register")
	@Operation(
		summary = "회원가입",
		description = "유저 정보를 입력받아 회원가입을 진행합니다.",
		security = {},
		responses = {
			@ApiResponse(responseCode = "201", description = "로그인 성공"),
			@ApiResponse(responseCode = "400", description = "잘못된 요청"),
			@ApiResponse(responseCode = "409", description = "providerId 값이 동일한 계정이 있을 경우"),
			@ApiResponse(responseCode = "500", description = "서버 오류")
		}
	)
	public ResTemplate<RegisterUserResponse> getUserInfoToRegister(@RequestBody RegisterUserRequest request) {
		RegisterUserResponse data = userService.registerUser(request);
		return new ResTemplate<>(HttpStatus.CREATED, "회원가입 성공", data);
	}

	@PostMapping("/chkPwd")
	@Operation(
		summary = "비밀번호 검증",
		description = "입력된 비밀번호가 사용자 계정의 비밀번호와 일치하는지 확인합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "비밀번호 일치"),
			@ApiResponse(responseCode = "400", description = "비밀번호 불일치 또는 잘못된 형식"),
			@ApiResponse(responseCode = "401", description = "엑세스 토큰 만료"),
			@ApiResponse(responseCode = "500", description = "서버 오류")
		}
	)
	public ResTemplate<Void> checkPassword(@RequestBody ChkPwdRequest request, Principal principal) {
		userService.checkPassword(request, principal);
		 return new ResTemplate<>(HttpStatus.OK, "비밀번호 일치", null);
	}

	// 비밀번호 변경
	// 회원탈퇴
	// 마이페이지 조회
	// 휴대폰번호 변경
}
