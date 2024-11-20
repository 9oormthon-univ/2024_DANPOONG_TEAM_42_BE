package com.groom.swipo.domain.payment.dto.request;

import com.groom.swipo.domain.payment.entity.PayChargelist;
import com.groom.swipo.domain.user.entity.User;

public record PayChargeRequest(
	Integer price,
	String impUid,
	String merchantUid
) {
	public PayChargelist toEntity(User user) {
		return PayChargelist.builder()
			.name("스위페이 충전")
			.price(this.price)
			.impUid(this.impUid)
			.merchantUid(this.merchantUid)
			.user(user)
			.build();
	}
}
