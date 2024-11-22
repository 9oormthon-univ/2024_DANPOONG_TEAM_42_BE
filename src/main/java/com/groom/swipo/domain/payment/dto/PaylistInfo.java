package com.groom.swipo.domain.payment.dto;

import java.time.LocalDateTime;

import com.groom.swipo.domain.payment.entity.Paylist;
import com.groom.swipo.domain.store.entity.Store;

import lombok.Builder;

@Builder
public record PaylistInfo(
	String paylistId,
	Integer amount,
	String storeName,
	LocalDateTime createAt
) {
	public static PaylistInfo of(Paylist paylist, Store store){
		return PaylistInfo.builder()
			.paylistId(String.valueOf(paylist.getId()))
			.amount((int)paylist.getValue())
			.storeName(store.getName())
			.createAt(paylist.getCreatedAt())
			.build();
	}
}
