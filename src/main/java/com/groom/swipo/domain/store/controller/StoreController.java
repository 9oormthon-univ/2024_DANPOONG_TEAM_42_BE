package com.groom.swipo.domain.store.controller;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.groom.swipo.domain.store.dto.request.ReviewsRegisterRequest;
import com.groom.swipo.domain.store.service.ReviewsService;
import com.groom.swipo.domain.store.service.WishlistService;
import com.groom.swipo.global.template.ResTemplate;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/store")
@Tag(name = "가게", description = "가게 관련 API 그룹")
public class StoreController {

	private final WishlistService wishlistService;
	private final ReviewsService reviewsService;

	@PostMapping("/wish")
	@Operation(
		summary = "관심 등록/해제",
		description = "해당 가게에 대해 관심 등록/해제를 반영합니다.",
		responses = {
			@ApiResponse(responseCode = "204", description = "관심 등록/해제 성공"),
			@ApiResponse(responseCode = "400", description = "잘못된 요청"),
			@ApiResponse(responseCode = "401", description = "인증되지 않은 요청"),
			@ApiResponse(responseCode = "403", description = "페이지 접근 권한이 없음"),
			@ApiResponse(responseCode = "404", description = "요청한 리소스를 찾을 수 없음"),
			@ApiResponse(responseCode = "500", description = "서버 오류")
		}
	)
	public ResTemplate<Void> updateWish(@RequestParam(name = "storeId") Long storeId,
		@RequestParam(name = "isWish") boolean isWish, Principal principal) {
		wishlistService.updateWish(storeId, isWish, principal);
		return new ResTemplate<>(HttpStatus.NO_CONTENT, "관심 등록/해제 성공");
	}

	@PostMapping("/reviews")
	@Operation(
		summary = "리뷰 등록",
		description = "해당 가게에 대한 리뷰를 등록합니다.",
		responses = {
			@ApiResponse(responseCode = "204", description = "리뷰 등록 성공"),
			@ApiResponse(responseCode = "400", description = "잘못된 요청"),
			@ApiResponse(responseCode = "401", description = "인증되지 않은 요청"),
			@ApiResponse(responseCode = "403", description = "페이지 접근 권한이 없음"),
			@ApiResponse(responseCode = "404", description = "요청한 리소스를 찾을 수 없음"),
			@ApiResponse(responseCode = "500", description = "서버 오류")
		}
	)
	public ResTemplate<Void> registerReview(@RequestBody ReviewsRegisterRequest request, Principal principal) {
		reviewsService.registerReview(request, principal);
		return new ResTemplate<>(HttpStatus.NO_CONTENT, "리뷰 등록 성공");
	}
}
