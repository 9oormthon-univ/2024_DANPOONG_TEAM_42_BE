package com.groom.swipo.domain.payment.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.groom.swipo.domain.payment.entity.PayChargelist;

import lombok.Builder;

@Builder
public record PayChargeResponse(Long id, String name, Integer price, Integer updatedTotalPay,
								@JsonFormat(pattern = "yyyy-MM-dd - HH:mm") LocalDateTime createdAt) {
	public static PayChargeResponse from(PayChargelist payChargelist) {
		return PayChargeResponse.builder()
			.id(payChargelist.getId())
			.name(payChargelist.getName())
			.price(payChargelist.getPrice())
			.createdAt(payChargelist.getCreatedAt())
			.updatedTotalPay(payChargelist.getUser().getPay().getTotalPay())
			.build();
	}
}
