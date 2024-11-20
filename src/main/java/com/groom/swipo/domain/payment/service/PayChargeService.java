package com.groom.swipo.domain.payment.service;

import java.math.BigDecimal;
import java.security.Principal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.groom.swipo.domain.payment.dto.request.PayChargeRequest;
import com.groom.swipo.domain.payment.dto.response.PayChargeResponse;
import com.groom.swipo.domain.payment.entity.PayChargelist;
import com.groom.swipo.domain.payment.exception.PaymentCompletionException;
import com.groom.swipo.domain.payment.repository.PayChargelistRepository;
import com.groom.swipo.domain.user.entity.User;
import com.groom.swipo.domain.user.exception.UserNotFoundException;
import com.groom.swipo.domain.user.repository.UserRepository;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PayChargeService {

	private final IamportClient iamportClient;
	private final UserRepository userRepository;
	private final PayChargelistRepository payChargelistRepository;

	@Transactional
	public PayChargeResponse payCharge(PayChargeRequest request, Principal principal) {
		Long userId = Long.parseLong(principal.getName());
		User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

		validate(request.impUid(), request.price());

		PayChargelist payChargelist = request.toEntity(user);
		payChargelistRepository.save(payChargelist);

		user.getPay().updatePay(request.price());
		return PayChargeResponse.from(payChargelist);
	}

	private void validate(String impUid, Integer price) {
		IamportResponse<Payment> response = iamportClient.paymentByImpUid(impUid);
		Payment payment = response.getResponse();

		if (payment == null || !"paid".equals(payment.getStatus())) {
			throw new PaymentCompletionException();
		}

		if (!payment.getAmount().equals(BigDecimal.valueOf(price))) {
			iamportClient.cancelPaymentByImpUid(new CancelData(impUid, true));
			throw new PaymentCompletionException();
		}
	}
}
