package com.groom.swipo.domain.point.dto.Response;

import com.groom.swipo.domain.point.entity.Card;

import lombok.Builder;

@Builder
public record PointTransferResponse(
	String fromCardId,
	String toCardId,
	Integer fromPoint,
	Integer toPoint
) {
	public static PointTransferResponse of(Card fromCard, Card toCard) {
		return PointTransferResponse.builder()
			.fromCardId(String.valueOf(fromCard.getId()))
			.toCardId(String.valueOf(toCard.getId()))
			.fromPoint(fromCard.getTotalPoint())
			.toPoint(toCard.getTotalPoint())
			.build();
	}
}
