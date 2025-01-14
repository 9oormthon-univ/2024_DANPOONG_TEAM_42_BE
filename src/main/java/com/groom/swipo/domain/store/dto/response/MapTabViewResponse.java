package com.groom.swipo.domain.store.dto.response;

import java.util.List;

import com.groom.swipo.domain.store.dto.StoreTabInfo;

import lombok.Builder;

@Builder
public record MapTabViewResponse(
	List<StoreTabInfo> wishlists,
	List<StoreTabInfo> picks,
	List<StoreTabInfo> trends,
	List<StoreTabInfo> tastes,
	List<StoreTabInfo> labs
) {
	public static MapTabViewResponse of(List<StoreTabInfo> wishlists, List<StoreTabInfo> picks,
		List<StoreTabInfo> trends, List<StoreTabInfo> tastes, List<StoreTabInfo> labs) {
		return MapTabViewResponse.builder()
			.wishlists(wishlists)
			.picks(picks)
			.trends(trends)
			.tastes(tastes)
			.labs(labs)
			.build();
	}
}
