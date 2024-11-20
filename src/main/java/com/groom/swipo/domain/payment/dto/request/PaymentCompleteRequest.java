package com.groom.swipo.domain.payment.dto.request;

public record PaymentCompleteRequest(
	Long storeId,
	String password,
	Integer amount,
	Integer usedPoints
) {
}
