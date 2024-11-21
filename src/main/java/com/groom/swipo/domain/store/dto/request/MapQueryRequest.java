package com.groom.swipo.domain.store.dto.request;

public record MapQueryRequest(
	Double latitude,
	Double longitude,
	Double radius
) {
}
