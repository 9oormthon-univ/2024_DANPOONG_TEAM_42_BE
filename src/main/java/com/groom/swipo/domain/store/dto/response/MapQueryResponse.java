package com.groom.swipo.domain.store.dto.response;

import java.util.List;

import com.groom.swipo.domain.store.dto.StoreInfo;

import lombok.Builder;

@Builder
public record MapQueryResponse(
	List<StoreInfo> wishlist,
	List<StoreInfo> nonWishlist
) {
	public static MapQueryResponse of(List<StoreInfo> wishlist, List<StoreInfo> nonWishlist) {
		return MapQueryResponse.builder()
			.wishlist(wishlist)
			.nonWishlist(nonWishlist)
			.build();
	}
}
