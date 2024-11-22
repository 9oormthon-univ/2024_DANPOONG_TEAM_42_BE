package com.groom.swipo.domain.point.controller;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.groom.swipo.domain.point.dto.Response.SwipstoneResponse;
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

	@GetMapping("/swipstone")
	@Operation(
		summary = "스윕스톤 조회",
		description = "내가 보유한 조각을 조회하여 스윕스톤을 교환할 수 있는지 조회합니다.",
		security = {},
		responses = {
			@ApiResponse(responseCode = "200", description = "조회 성공"),
			@ApiResponse(responseCode = "400", description = "잘못된 요청"),
			@ApiResponse(responseCode = "401", description = "인증되지 않은 요청"),
			@ApiResponse(responseCode = "404", description = "보유한 조각이 없습니다"),
			@ApiResponse(responseCode = "500", description = "서버 오류")
		}
	)
	public ResTemplate<SwipstoneResponse> getSwipstone(Principal principal) {
		SwipstoneResponse data = pointService.getSwipstone(principal);
		return new ResTemplate<>(HttpStatus.OK, "조회 성공", data);
	}
}
