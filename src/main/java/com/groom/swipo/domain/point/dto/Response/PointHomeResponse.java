package com.groom.swipo.domain.point.dto.Response;

import java.util.List;

import com.groom.swipo.domain.payment.dto.PaylistInfo;
import com.groom.swipo.domain.payment.entity.Pay;
import com.groom.swipo.domain.point.dto.CardInfo;

import lombok.Builder;

@Builder
public record PointHomeResponse(
	String userName,
	Integer balance, // 페이잔액
	Integer totalCards, //사용자가 보유한 모든 지역 카드 수
	List<CardInfo> cards,
	List<PaylistInfo> paylistInfos
) {
	public static PointHomeResponse of(String userName, Pay pay, Integer totalCards, List<CardInfo> cards,
		List<PaylistInfo> paylistInfos) {
		return PointHomeResponse.builder()
			.userName(userName)
			.balance(pay.getTotalPay())
			.totalCards(totalCards)
			.cards(cards)
			.paylistInfos(paylistInfos)
			.build();
	}
}
