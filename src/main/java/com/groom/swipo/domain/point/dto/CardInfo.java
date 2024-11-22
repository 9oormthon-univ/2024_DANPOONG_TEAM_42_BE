package com.groom.swipo.domain.point.dto;

import com.groom.swipo.domain.point.entity.Card;

import lombok.Builder;

@Builder
public record CardInfo(
	String cardId,
	String region,
	Integer point,
	String customImage
) {
	public static CardInfo of(Card card) {
		return CardInfo.builder()
			.cardId(String.valueOf(card.getId()))
			.region(card.getArea().getRegionName())
			.point(card.getTotalPoint())
			.customImage(card.getCustomImage())
			.build();
	}
}
