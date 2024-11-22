package com.groom.swipo.domain.point.dto.Response;

import java.util.List;

import com.groom.swipo.domain.point.dto.PieceInfo;

import lombok.Builder;

@Builder
public record SwipstoneResponse(
	Integer picesNum,
	List<PieceInfo> pieces
){
	public static SwipstoneResponse of(Integer picesNum, List<PieceInfo> pieces) {
		return SwipstoneResponse.builder()
			.picesNum(picesNum)
			.pieces(pieces)
			.build();
	}
}
