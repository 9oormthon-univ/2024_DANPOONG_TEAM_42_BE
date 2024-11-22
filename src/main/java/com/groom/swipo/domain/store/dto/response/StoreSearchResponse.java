package com.groom.swipo.domain.store.dto.response;

import java.util.List;

import com.groom.swipo.domain.store.dto.StoreDetail;
import com.groom.swipo.global.common.PageInfo;

import lombok.Builder;

@Builder
public record StoreSearchResponse(
	PageInfo pageInfo,
	List<StoreDetail> stores
) {
	public static StoreSearchResponse of(PageInfo pageInfo, List<StoreDetail> stores) {
		return StoreSearchResponse.builder()
			.pageInfo(pageInfo)
			.stores(stores)
			.build();
	}
}
