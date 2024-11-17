package com.groom.swipo.global.error.dto;

public record ErrorResponse(
	int code,
	String message
) {
}