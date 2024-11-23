package com.groom.swipo.domain.point.dto.Response;

import java.util.List;

import com.groom.swipo.domain.point.dto.CardInfo;

import lombok.Builder;

@Builder
public record PointTransferInfoResponse(
	Integer cardNum,
	List<CardInfo> cards
) {
	public static PointTransferInfoResponse of(Integer cardNum, List<CardInfo> cards){
		return PointTransferInfoResponse.builder()
			.cardNum(cardNum)
			.cards(cards)
			.build();
	}
}
