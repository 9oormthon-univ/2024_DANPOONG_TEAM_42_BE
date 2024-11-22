package com.groom.swipo.global.common;

import org.springframework.data.domain.Page;

import lombok.Builder;

@Builder
public record PageInfo(
	int currentPage,
	int totalPages,
	long totalItems
) {
	public static <T> PageInfo from(Page<T> entityPage) {
		return PageInfo.builder()
			.currentPage(entityPage.getNumber())
			.totalPages(entityPage.getTotalPages())
			.totalItems(entityPage.getTotalElements())
			.build();
	}
}
