package com.groom.swipo.domain.user.controller;

import java.security.Principal;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import com.groom.swipo.domain.user.dto.request.PhoneCheckRequest;
import com.groom.swipo.domain.user.dto.request.PhoneVerificationRequest;
import com.groom.swipo.domain.user.dto.request.PwdRequest;
import com.groom.swipo.domain.user.dto.request.RegisterUserRequest;
import com.groom.swipo.domain.user.dto.response.RegisterUserResponse;
import com.groom.swipo.domain.user.service.SmsService;
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
	private final SmsService smsService;
	private final KakaoLoginService kakaoLoginService;
	private final TokenRenewService tokenRenewService;
	private final AppleLoginService appleLoginService;

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
	public ResTemplate<SocialLoginResponse> kakaoLogin(@RequestBody SocialLoginRequest request) {
		SocialLoginResponse data = kakaoLoginService.kakaoLogin(request.code());
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

	@PostMapping("/phone")
	@Operation(
		summary = "휴대폰 인증",
		description = "휴대폰 번호를 받아 해당 번호로 인증코드 발송",
		security = {},
		responses = {
			@ApiResponse(responseCode = "201", description = "로그인 성공"),
			@ApiResponse(responseCode = "400", description = "잘못된 요청"),
			@ApiResponse(responseCode = "500", description = "서버 오류")
		}
	)
	public ResTemplate<Void> getPhoneCheck(@RequestBody PhoneCheckRequest request) {
		LocalDateTime sentAt = LocalDateTime.now();
		smsService.sendVerificationMessage(request.phone(), sentAt); // 인스턴스 메서드 호출
		return new ResTemplate<>(HttpStatus.OK, "송신 완료", null);
	}

	@PostMapping("/phone-verification")
	@Operation(
		summary = "휴대폰 인증",
		description = "사용자가 받은 인증코드와 서버에서 보낸 인증코드를 대조하여 검증",
		security = {},
		responses = {
			@ApiResponse(responseCode = "201", description = "로그인 성공"),
			@ApiResponse(responseCode = "400", description = "인증번호가 틀렸을 경우"),
			@ApiResponse(responseCode = "408", description = "인증 시간이 만료되었을 경우"),
			@ApiResponse(responseCode = "500", description = "서버 오류")
		}
	)
	public ResTemplate<Void> verificationByCode(@RequestBody PhoneVerificationRequest request) {
		LocalDateTime verifiedAt = LocalDateTime.now();
		smsService.verifyCode(request.phone(), request.code(), verifiedAt);
		return new ResTemplate<>(HttpStatus.OK, "인증 완료", null);
	}

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
			@ApiResponse(responseCode = "401", description = "인증되지 않은 요청"),
			@ApiResponse(responseCode = "500", description = "서버 오류")
		}
	)
	public ResTemplate<Void> checkPassword(@RequestBody PwdRequest request, Principal principal) {
		userService.checkPassword(request, principal);
		 return new ResTemplate<>(HttpStatus.OK, "비밀번호 일치", null);
	}

	// 비밀번호 변경
	@PostMapping("/editPwd")
	@Operation(
		summary = "비밀번호 검증",
		description = "기존 비밀번호를 입력된 비밀번호로 변경합니다",
		responses = {
			@ApiResponse(responseCode = "200", description = "비밀번호 일치"),
			@ApiResponse(responseCode = "400", description = "비밀번호 불일치 또는 잘못된 형식"),
			@ApiResponse(responseCode = "401", description = "인증되지 않은 요청"),
			@ApiResponse(responseCode = "409", description = "이전 비밀번호와 동일한 경우"),
			@ApiResponse(responseCode = "500", description = "서버 오류")
		}
	)
	public ResTemplate<Void> editPassword(@RequestBody PwdRequest request, Principal principal) {
		userService.editPassword(request, principal);
		return new ResTemplate<>(HttpStatus.OK, "비밀번호 변경완료", null);
	}
	// 회원탈퇴
	@DeleteMapping("/delete")
	@Operation(
		summary = "회원 탈퇴",
		description = "회원을 완전히 탈퇴하고, Redis에서 jwt 토큰도 삭제",
		responses = {
			@ApiResponse(responseCode = "200", description = "회원탈퇴 완료"),
			@ApiResponse(responseCode = "400", description = "비밀번호 불일치 또는 잘못된 형식"),
			@ApiResponse(responseCode = "401", description = "인증되지 않은 요청"),
			@ApiResponse(responseCode = "500", description = "서버 오류")
		}
	)
	public ResTemplate<Void> deleteUser(Principal principal) {
		userService.deleteUser(principal);
		return new ResTemplate<>(HttpStatus.OK, "탈퇴완료", null);
	}


	// 마이페이지 조회
}
