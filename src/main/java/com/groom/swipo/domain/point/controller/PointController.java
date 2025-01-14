package com.groom.swipo.domain.point.controller;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.groom.swipo.domain.point.dto.Request.PointTransferRequest;
import com.groom.swipo.domain.point.dto.Request.SwipstoneSwapRequest;
import com.groom.swipo.domain.point.dto.Response.PointHomeResponse;
import com.groom.swipo.domain.point.dto.Response.PointTransferResponse;
import com.groom.swipo.domain.point.dto.Response.PointTransferInfoResponse;
import com.groom.swipo.domain.point.dto.Response.SwipstoneResponse;
import com.groom.swipo.domain.point.dto.Response.SwipstoneSwapResponse;
import com.groom.swipo.domain.point.service.PointService;
import com.groom.swipo.global.template.ResTemplate;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/point")
@Tag(name = "포인트", description = "포인트 관련 API 그룹")
public class PointController {
	private final PointService pointService;

	@GetMapping("/home")
	@Operation(
		summary = "스윕페이/포인트 홈 조회",
		description = "상단 스웹페이 탭 클릭시 조회되는 정보 제공",
		security = {},
		responses = {
			@ApiResponse(responseCode = "200", description = "조회 성공"),
			@ApiResponse(responseCode = "400", description = "잘못된 요청"),
			@ApiResponse(responseCode = "401", description = "인증되지 않은 요청"),
			@ApiResponse(responseCode = "500", description = "서버 오류")
		}
	)
	public ResTemplate<PointHomeResponse> getHome(Principal principal) {
		PointHomeResponse data = pointService.getHome(principal);
		return new ResTemplate<>(HttpStatus.OK, "조회 성공", data);
	}

	@PostMapping("/card-register")
	@Operation(
		summary = "포인트 카드 등록",
		description = "지역 정보와 이미지 파일을 사용하여 포인트 카드를 등록합니다.",
		security = {},
		responses = {
			@ApiResponse(responseCode = "201", description = "카드 등록 성공"),
			@ApiResponse(responseCode = "400", description = "유효하지 않은 동네"),
			@ApiResponse(responseCode = "401", description = "유효하지 않은 인증 토큰"),
			@ApiResponse(responseCode = "409", description = "중복 카드 등록"),
			@ApiResponse(responseCode = "500", description = "서버 오류")
		}
	)
	public ResTemplate<Void> registerCard(
		@RequestPart("region") String region,
		@RequestPart(value = "custom_image", required = false) MultipartFile customImage,
		Principal principal) {

		pointService.registerCard(region, customImage, principal);
		return new ResTemplate<>(HttpStatus.CREATED, "카드 등록 성공");
	}

	@GetMapping("/swipstone")
	@Operation(
		summary = "스윕스톤 조회",
		description = "내가 보유한 조각을 조회하여 스윕스톤을 교환할 수 있는지 조회합니다.",
		security = {},
		responses = {
			@ApiResponse(responseCode = "200", description = "조회 성공"),
			@ApiResponse(responseCode = "400", description = "잘못된 요청"),
			@ApiResponse(responseCode = "401", description = "인증되지 않은 요청"),
			@ApiResponse(responseCode = "404", description = "보유한 조각이 없는 경우"),
			@ApiResponse(responseCode = "500", description = "서버 오류")
		}
	)
	public ResTemplate<SwipstoneResponse> getSwipstone(Principal principal) {
		SwipstoneResponse data = pointService.getSwipstone(principal);
		return new ResTemplate<>(HttpStatus.OK, "조회 성공", data);
	}

	@PostMapping("/swipstone-swap")
	@Operation(
		summary = "스윕스톤 교환",
		description = "보유한 조각들이 스윕스톤을 완성하였을때 페이로 교환됩니다.",
		security = {},
		responses = {
			@ApiResponse(responseCode = "200", description = "교환 성공"),
			@ApiResponse(responseCode = "400", description = "잘못된 요청"),
			@ApiResponse(responseCode = "401", description = "인증되지 않은 요청"),
			@ApiResponse(responseCode = "404", description = "보유한 조각이 부족하거나 없는 경우"),
			@ApiResponse(responseCode = "500", description = "서버 오류")
		}
	)
	public ResTemplate<SwipstoneSwapResponse> swapSwipstone(@RequestBody SwipstoneSwapRequest resquest,
		Principal principal) {
		SwipstoneSwapResponse data = pointService.swapSwipstone(resquest, principal);
		return new ResTemplate<>(HttpStatus.OK, "교환 성공", data);
	}

	@GetMapping("/transfer-info")
	@Operation(
		summary = "포인트 이전 조희",
		description = "포인트 이전 페이지에 들어왔을떄 자신이 보유한 포인트 카드들 조회 가능",
		security = {},
		responses = {
			@ApiResponse(responseCode = "200", description = "교환 성공"),
			@ApiResponse(responseCode = "400", description = "잘못된 요청"),
			@ApiResponse(responseCode = "401", description = "인증되지 않은 요청"),
			@ApiResponse(responseCode = "500", description = "서버 오류")
		}
	)
	public ResTemplate<PointTransferInfoResponse> getPointTransferInfo(Principal principal) {
		PointTransferInfoResponse data= pointService.getPointTransferInfo(principal);
		return new ResTemplate<>(HttpStatus.OK, "내 카드 조회 성공", data);
	}

	@PostMapping("/transefer")
	@Operation(
		summary = "포인트 이전",
		description = "포인트 이전 페이지에서 포인트 이전을 클릭 시 ",
		security = {},
		responses = {
			@ApiResponse(responseCode = "200", description = "이전 성공"),
			@ApiResponse(responseCode = "400", description = "잘못된 요청"),
			@ApiResponse(responseCode = "401", description = "인증되지 않은 요청"),
			@ApiResponse(responseCode = "500", description = "서버 오류")
		}
	)
	public ResTemplate<PointTransferResponse> pointTransfer(@RequestBody PointTransferRequest request, Principal principal) {
		PointTransferResponse data = pointService.pointTransfer(request, principal);
		return new ResTemplate<>(HttpStatus.OK, "이전 성공", data);
	}
}
