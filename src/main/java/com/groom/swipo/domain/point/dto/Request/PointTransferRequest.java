package com.groom.swipo.domain.point.dto.Request;

public record PointTransferRequest(
	String fromCardId,
	String toCardId,
	Integer point
){
}
