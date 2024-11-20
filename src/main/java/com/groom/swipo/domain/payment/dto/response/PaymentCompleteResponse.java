package com.groom.swipo.domain.payment.dto.response;

import lombok.Builder;

@Builder
public record PaymentCompleteResponse(
	Integer amount,
	Integer earnedPoints,
	Integer totalPay
) {
	public static PaymentCompleteResponse of(Integer amount, Integer earnedPoints, Integer totalPay) {
		return PaymentCompleteResponse.builder()
			.amount(amount)
			.earnedPoints(earnedPoints)
			.totalPay(totalPay)
			.build();
	}
}
