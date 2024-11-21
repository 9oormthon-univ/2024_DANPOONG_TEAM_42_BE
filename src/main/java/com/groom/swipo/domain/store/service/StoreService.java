package com.groom.swipo.domain.store.service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.groom.swipo.domain.store.dto.StoreInfo;
import com.groom.swipo.domain.store.dto.response.MapQueryResponse;
import com.groom.swipo.domain.store.dto.response.MapStoreDetailResponse;
import com.groom.swipo.domain.store.entity.Reviews;
import com.groom.swipo.domain.store.entity.Store;
import com.groom.swipo.domain.store.entity.StoreImage;
import com.groom.swipo.domain.store.entity.Wishlist;
import com.groom.swipo.domain.store.exception.StoreNotFoundException;
import com.groom.swipo.domain.store.repository.ReviewsRepository;
import com.groom.swipo.domain.store.repository.StoreImageRepository;
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
	private final WishilistRepository wishilistRepository;
	private final StoreRepository storeRepository;
	private final StoreImageRepository storeImageRepository;
	private final ReviewsRepository reviewsRepository;

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

	public MapStoreDetailResponse getStoreDetails(Long storeId, Principal principal) {
		Long userId = Long.parseLong(principal.getName());
		User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
		Store store = storeRepository.findById(storeId).orElseThrow(StoreNotFoundException::new);

		// 해당 가게 리뷰 조회
		List<Reviews> reviews = reviewsRepository.findAllByStore(store);

		// 평균 별점 계산 (소수 첫째 자리까지 반올림)
		Double averageStars = reviews.stream()
			.mapToDouble(Reviews::getStar)
			.average()
			.stream()
			.map(avg -> Math.round(avg * 10) / 10.0)
			.findFirst()
			.orElse(0.0);

		// 가게 이미지
		List<StoreImage> images = storeImageRepository.findAllByStore(store);

		// 사용자가 관심 등록했는지 여부
		boolean isWish = wishilistRepository.findByUserAndStore(user, store)
			.map(Wishlist::isWish)
			.orElse(false);

		return MapStoreDetailResponse.of(store, averageStars, isWish, reviews, images);
	}
}
