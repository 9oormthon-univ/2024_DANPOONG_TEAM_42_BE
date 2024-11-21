package com.groom.swipo.domain.store.dto;

import com.groom.swipo.domain.store.entity.Store;
import com.groom.swipo.domain.store.entity.enums.StoreType;

import lombok.Builder;

@Builder
public record StoreInfo(
	Long storeId,
	Integer percent,
	StoreType type,
	Double latitude,
	Double longitude
) {
	public static StoreInfo from(Store store) {
		return StoreInfo.builder()
			.storeId(store.getId())
			.percent(store.getPercent())
			.type(store.getType())
			.latitude(store.getLatitude())
			.longitude(store.getLongitude())
			.build();
	}
}
