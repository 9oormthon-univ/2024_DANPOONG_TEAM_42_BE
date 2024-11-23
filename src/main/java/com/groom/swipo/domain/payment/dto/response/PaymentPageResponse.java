package com.groom.swipo.domain.payment.dto.response;

import com.groom.swipo.domain.point.entity.Card;
import com.groom.swipo.domain.store.entity.Store;
import com.groom.swipo.domain.user.entity.User;

import lombok.Builder;

@Builder
public record PaymentPageResponse(
	Integer totalPoint,
	Integer totalPay,
	Integer percent,
	String customImage
) {
	public static PaymentPageResponse of(Card card, User user, Store store) {
		return PaymentPageResponse.builder()
			.totalPoint(card.getTotalPoint())
			.totalPay(user.getPay().getTotalPay())
			.percent(store.getPercent())
			.customImage(card.getCustomImage())
			.build();
	}
}
