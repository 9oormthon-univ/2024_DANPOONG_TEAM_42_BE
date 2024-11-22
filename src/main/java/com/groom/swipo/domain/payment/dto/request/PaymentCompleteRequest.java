package com.groom.swipo.domain.payment.dto.request;

import com.groom.swipo.domain.payment.entity.Pay;
import com.groom.swipo.domain.payment.entity.Paylist;
import com.groom.swipo.domain.store.entity.Store;

public record PaymentCompleteRequest(
	Long storeId,
	String password,
	Integer amount,
	Integer usedPoints
) {
	public Paylist toEntity(Pay pay, Store store) {
		return Paylist.builder()
			.value(amount - usedPoints)
			.pay(pay)
			.store(store)
			.build();
	}
}
