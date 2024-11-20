package com.groom.swipo.domain.payment.controller;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.groom.swipo.domain.payment.dto.request.PayChargeRequest;
import com.groom.swipo.domain.payment.dto.request.PaymentCompleteRequest;
import com.groom.swipo.domain.payment.dto.response.PayChargeResponse;
import com.groom.swipo.domain.payment.dto.response.PaymentCompleteResponse;
import com.groom.swipo.domain.payment.dto.response.PaymentPageResponse;
import com.groom.swipo.domain.payment.service.PayChargeService;
import com.groom.swipo.domain.payment.service.PaymentService;
import com.groom.swipo.global.template.ResTemplate;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/payment")
@Tag(name = "결제", description = "결제 관련 API 그룹")
public class PaymentController {

	private final PayChargeService payChargeService;
	private final PaymentService paymentService;

	@PostMapping("/charge")
	@Operation(
		summary = "스위페이 충전",
		description = "요청한 금액으로 스웨페이 충전을 진행합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "스위페이 충전 성공"),
			@ApiResponse(responseCode = "400", description = "잘못된 요청"),
			@ApiResponse(responseCode = "401", description = "인증되지 않은 요청"),
			@ApiResponse(responseCode = "403", description = "페이지 접근 권한이 없음"),
			@ApiResponse(responseCode = "404", description = "요청한 리소스를 찾을 수 없음"),
			@ApiResponse(responseCode = "500", description = "서버 오류")
		}
	)
	public ResTemplate<PayChargeResponse> payCharge(@RequestBody PayChargeRequest request, Principal principal) {
		PayChargeResponse data = payChargeService.payCharge(request, principal);
		return new ResTemplate<>(HttpStatus.OK, "스웨페이 충전 성공", data);
	}

	@GetMapping("/page")
	@Operation(
		summary = "결제 페이지 조회",
		description = "특정 가게 ID로 해당 지역의 보유 포인트, 스위페이 잔액, 포인트 적립 비율을 반환합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "결제 페이지 조회 성공"),
			@ApiResponse(responseCode = "400", description = "잘못된 요청"),
			@ApiResponse(responseCode = "401", description = "인증되지 않은 요청"),
			@ApiResponse(responseCode = "403", description = "페이지 접근 권한이 없음"),
			@ApiResponse(responseCode = "404", description = "요청한 리소스를 찾을 수 없음"),
			@ApiResponse(responseCode = "500", description = "서버 오류")
		}
	)
	public ResTemplate<PaymentPageResponse> getPaymentPage(@RequestParam(name = "storeId") Long storeId, Principal principal) {
		PaymentPageResponse data = paymentService.getPaymentPage(storeId, principal);
		return new ResTemplate<>(HttpStatus.OK, "결제 페이지 조회 성공", data);
	}

	@PostMapping("/complete")
	public ResTemplate<PaymentCompleteResponse> completePayment(@RequestBody PaymentCompleteRequest request, Principal principal) {
		PaymentCompleteResponse data = paymentService.completePayment(request, principal);
		return new ResTemplate<>(HttpStatus.OK, "결제 완료", data);
	}
}
