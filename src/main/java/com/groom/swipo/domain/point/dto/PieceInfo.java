package com.groom.swipo.domain.point.dto;

import com.groom.swipo.domain.point.entity.Piece;

import lombok.Builder;

@Builder
public record PieceInfo(
	String pieceId,
	String pieceName
) {
	public static PieceInfo from(Piece piece) {
		return PieceInfo.builder()
			.pieceId(String.valueOf(piece.getId()))
			.pieceName(piece.getName())
			.build();
	}
}
