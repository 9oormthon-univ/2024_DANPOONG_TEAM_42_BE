package com.groom.swipo.domain.store.dto;

import java.util.List;

import com.groom.swipo.domain.store.entity.Store;

import lombok.Builder;

@Builder
public record StoreDetail(
	Long storeId,
	String name,
	String address,
	Integer percent,
	Double stars,
	boolean isWish,
	Integer reviewsNum,
	String review,
	Integer imagesNum,
	List<String> imageUrls
) {
	public static StoreDetail of(Store store, Double stars, boolean isWish, String review, List<String> images) {
		return StoreDetail.builder()
			.storeId(store.getId())
			.name(store.getName())
			.address(store.getAddress())
			.percent(store.getPercent())
			.stars(stars)
			.isWish(isWish)
			.reviewsNum(store.getReviews().size())
			.review(review)
			.imagesNum(images.size())
			.imageUrls(images)
			.build();
	}
}
