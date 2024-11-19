package com.groom.swipo.domain.payment.controller;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.groom.swipo.domain.payment.dto.request.PayChargeRequest;
import com.groom.swipo.domain.payment.dto.response.PayChargeResponse;
import com.groom.swipo.domain.payment.service.PayChargeService;
import com.groom.swipo.global.template.ResTemplate;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/payment")
public class PaymentController {

	private final PayChargeService payChargeService;

	@PostMapping("/charge")
	public ResTemplate<PayChargeResponse> payCharge(@RequestBody PayChargeRequest request, Principal principal) {
		PayChargeResponse data = payChargeService.payCharge(request, principal);
		return new ResTemplate<>(HttpStatus.OK, "스웨페이 충전 성공", data);
	}
}
