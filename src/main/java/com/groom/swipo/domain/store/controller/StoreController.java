package com.groom.swipo.domain.store.controller;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.groom.swipo.domain.store.dto.request.ReviewsRegisterRequest;
import com.groom.swipo.domain.store.dto.response.MapQueryResponse;
import com.groom.swipo.domain.store.dto.response.MapStoreDetailResponse;
import com.groom.swipo.domain.store.dto.response.MapTabViewResponse;
import com.groom.swipo.domain.store.dto.response.StoreSearchResponse;
import com.groom.swipo.domain.store.service.ReviewsService;
import com.groom.swipo.domain.store.service.StoreService;
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
	private final StoreService storeService;

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

	@GetMapping("/map")
	@Operation(
		summary = "가게 데이터 조회",
		description = "관심 가게와 전체 가게 리스트를 조회합니다. 관심 가게로 분류되어 있으면, 전체 가게 리스트에는 중복으로 포함되지 않습니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "가게 데이터 조회 성공"),
			@ApiResponse(responseCode = "400", description = "잘못된 요청"),
			@ApiResponse(responseCode = "401", description = "인증되지 않은 요청"),
			@ApiResponse(responseCode = "403", description = "페이지 접근 권한이 없음"),
			@ApiResponse(responseCode = "404", description = "요청한 리소스를 찾을 수 없음"),
			@ApiResponse(responseCode = "500", description = "서버 오류")
		}
	)
	public ResTemplate<MapQueryResponse> getStores(Principal principal) {
		MapQueryResponse data = storeService.getStores(principal);
		return new ResTemplate<>(HttpStatus.OK, "가게 데이터 조회 성공", data);
	}

	@GetMapping("/details")
	@Operation(
		summary = "가게 상세 조회",
		description = "해당 가게 상세 정보를 조회합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "가게 상세 조회 성공"),
			@ApiResponse(responseCode = "400", description = "잘못된 요청"),
			@ApiResponse(responseCode = "401", description = "인증되지 않은 요청"),
			@ApiResponse(responseCode = "403", description = "페이지 접근 권한이 없음"),
			@ApiResponse(responseCode = "404", description = "요청한 리소스를 찾을 수 없음"),
			@ApiResponse(responseCode = "500", description = "서버 오류")
		}
	)
	public ResTemplate<MapStoreDetailResponse> getStoreDetails(@RequestParam(name = "storeId") Long storeId,
		Principal principal) {
		MapStoreDetailResponse data = storeService.getStoreDetails(storeId, principal);
		return new ResTemplate<>(HttpStatus.OK, "가게 상세 조회 성공", data);
	}

	@GetMapping("/tabs")
	@Operation(
		summary = "탭별 가게 조회",
		description = "관심 가게와 PICK, TREND, TASTE, LAB 카테고리별로 대표 가게 리스트를 조회합니다. 관심 가게와 PICK은 3개, 나머지 카테고리는 5개씩 반환합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "탭별 가게 조회 성공"),
			@ApiResponse(responseCode = "400", description = "잘못된 요청"),
			@ApiResponse(responseCode = "401", description = "인증되지 않은 요청"),
			@ApiResponse(responseCode = "403", description = "페이지 접근 권한이 없음"),
			@ApiResponse(responseCode = "404", description = "요청한 리소스를 찾을 수 없음"),
			@ApiResponse(responseCode = "500", description = "서버 오류")
		}
	)
	public ResTemplate<MapTabViewResponse> getStoreTabs(Principal principal) {
		MapTabViewResponse data = storeService.getStoreTabs(principal);
		return new ResTemplate<>(HttpStatus.OK, "탭별 가게 조회 성공", data);
	}

	@GetMapping("/search")
	public ResTemplate<StoreSearchResponse> searchStores(
		@RequestParam(name = "keyword") String keyword,
		@RequestParam(name = "category", defaultValue = "all") String category,
		@RequestParam(name = "type") String type,
		@RequestParam(name = "page", defaultValue = "0") int page,
		Principal principal
	) {
		StoreSearchResponse data = storeService.searchStores(keyword, category, type, page, principal);
		return new ResTemplate<>(HttpStatus.OK, "가게 검색 성공", data);
	}
}
