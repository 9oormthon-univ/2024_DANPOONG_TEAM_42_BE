package com.groom.swipo.domain.point.dto;

import com.groom.swipo.domain.point.entity.MyPiece;
import com.groom.swipo.domain.point.entity.Piece;

import lombok.Builder;

@Builder
public record PieceInfo(
	String myPieceId,
	String pieceName
) {
	public static PieceInfo from(MyPiece myPiece,Piece piece) {
		return PieceInfo.builder()
			.myPieceId(String.valueOf(myPiece.getId()))
			.pieceName(piece.getName())
			.build();
	}
}
