package com.groom.swipo.domain.store.service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.groom.swipo.domain.store.dto.StoreInfo;
import com.groom.swipo.domain.store.dto.response.MapQueryResponse;
import com.groom.swipo.domain.store.entity.Store;
import com.groom.swipo.domain.store.repository.StoreRepository;
import com.groom.swipo.domain.store.repository.WishilistRepository;
import com.groom.swipo.domain.user.entity.User;
import com.groom.swipo.domain.user.exception.UserNotFoundException;
import com.groom.swipo.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {

	private final UserRepository userRepository;
	private final StoreRepository storeRepository;
	private final WishilistRepository wishilistRepository;

	public MapQueryResponse getStores(Principal principal) {
		Long userId = Long.parseLong(principal.getName());
		User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

		List<Store> allStores = storeRepository.findAll();

		// 관심 등록된 가게 필터링
		List<StoreInfo> wishlist = allStores.stream()
			.filter(store -> wishilistRepository.existsByUserAndStoreAndIsWishTrue(user, store))
			.map(StoreInfo::from)
			.collect(Collectors.toList());

		// 관심 등록되지 않은 가게 필터링
		List<StoreInfo> nonWishlist = allStores.stream()
			.filter(store -> !wishilistRepository.existsByUserAndStoreAndIsWishTrue(user, store))
			.map(StoreInfo::from)
			.collect(Collectors.toList());

		return MapQueryResponse.of(wishlist, nonWishlist);
	}
}
