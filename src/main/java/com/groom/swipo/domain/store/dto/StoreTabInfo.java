package com.groom.swipo.domain.store.dto;

import com.groom.swipo.domain.store.entity.Store;

import lombok.Builder;

@Builder
public record StoreTabInfo(
	Long storeId,
	String name,
	String address,
	Integer percent,
	String reviewComment,
	String imageUrl
) {
	public static StoreTabInfo of(Store store, String reviewComment, String imageUrl) {
		return StoreTabInfo.builder()
			.storeId(store.getId())
			.name(store.getName())
			.address(store.getAddress())
			.percent(store.getPercent())
			.reviewComment(reviewComment)
			.imageUrl(imageUrl)
			.build();
	}
}
