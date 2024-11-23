package com.groom.swipo.domain.point.dto.Request;

import java.util.List;

public record SwipstoneSwapRequest(
	Integer point,
	Integer usePieceNum,
	List<String> myPieceIds
) {}

