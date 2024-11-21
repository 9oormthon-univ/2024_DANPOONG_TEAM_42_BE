package com.groom.swipo.domain.store.dto.request;

import com.groom.swipo.domain.store.entity.Reviews;
import com.groom.swipo.domain.store.entity.Store;
import com.groom.swipo.domain.user.entity.User;

public record ReviewsRegisterRequest(
	Long storeId,
	Double star,
	String comment
) {
	public Reviews toEntity(User user, Store store) {
		return Reviews.builder()
			.star(star)
			.comment(comment)
			.user(user)
			.store(store)
			.build();
	}
}
