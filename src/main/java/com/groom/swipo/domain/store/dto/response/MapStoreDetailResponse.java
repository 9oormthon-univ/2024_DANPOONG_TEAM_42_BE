package com.groom.swipo.domain.store.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import com.groom.swipo.domain.store.entity.Reviews;
import com.groom.swipo.domain.store.entity.Store;
import com.groom.swipo.domain.store.entity.StoreImage;

import lombok.Builder;

@Builder
public record MapStoreDetailResponse(Long storeId, String name, String address, Integer percent, Double stars,
									 boolean isWish, Integer reviewsNum, List<String> reviews, Integer imagesNum,
									 List<String> images) {
	public static MapStoreDetailResponse of(Store store, Double averageStars, boolean isWish, List<Reviews> reviews,
		List<StoreImage> images) {
		return MapStoreDetailResponse.builder()
			.storeId(store.getId())
			.name(store.getName())
			.address(store.getAddress())
			.percent(store.getPercent())
			.stars(averageStars)
			.isWish(isWish)
			.reviewsNum(reviews.size())
			.reviews(reviews.stream().map(Reviews::getComment).collect(Collectors.toList()))
			.imagesNum(images.size())
			.images(images.stream().map(StoreImage::getUrl).collect(Collectors.toList()))
			.build();
	}
}
