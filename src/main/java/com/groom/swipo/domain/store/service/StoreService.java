package com.groom.swipo.domain.store.service;

import java.security.Principal;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.groom.swipo.domain.store.dto.StoreDetail;
import com.groom.swipo.domain.store.dto.StoreInfo;
import com.groom.swipo.domain.store.dto.StoreTabInfo;
import com.groom.swipo.domain.store.dto.response.MapQueryResponse;
import com.groom.swipo.domain.store.dto.response.MapStoreDetailResponse;
import com.groom.swipo.domain.store.dto.response.MapTabViewResponse;
import com.groom.swipo.domain.store.dto.response.StoreSearchResponse;
import com.groom.swipo.domain.store.entity.Reviews;
import com.groom.swipo.domain.store.entity.Store;
import com.groom.swipo.domain.store.entity.StoreImage;
import com.groom.swipo.domain.store.entity.Wishlist;
import com.groom.swipo.domain.store.entity.enums.StoreCategory;
import com.groom.swipo.domain.store.entity.enums.StoreType;
import com.groom.swipo.domain.store.exception.StoreNotFoundException;
import com.groom.swipo.domain.store.repository.ReviewsRepository;
import com.groom.swipo.domain.store.repository.StoreImageRepository;
import com.groom.swipo.domain.store.repository.StoreRepository;
import com.groom.swipo.domain.store.repository.WishilistRepository;
import com.groom.swipo.domain.user.entity.User;
import com.groom.swipo.domain.user.exception.UserNotFoundException;
import com.groom.swipo.domain.user.repository.UserRepository;
import com.groom.swipo.global.common.PageInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {

	private static final int PAGE_SIZE = 5;

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

	public MapTabViewResponse getStoreTabs(Principal principal) {
		Long userId = Long.parseLong(principal.getName());
		User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

		List<Store> allStores = storeRepository.findAll();

		// 관심 등록된 가게 필터링 (최대 3개)
		List<StoreTabInfo> wishlists = allStores.stream()
			.filter(store -> wishilistRepository.existsByUserAndStoreAndIsWishTrue(user, store))
			.limit(3)
			.map(this::convertToTabInfo)
			.toList();

		// 관심 등록되지 않은 가게를 타입별로 그룹화
		Map<StoreType, List<StoreTabInfo>> groupedTabs = allStores.stream()
			.filter(store -> !wishilistRepository.existsByUserAndStoreAndIsWishTrue(user, store))
			.collect(Collectors.groupingBy(
				Store::getType,
				() -> new EnumMap<>(StoreType.class),
				Collectors.mapping(this::convertToTabInfo, Collectors.toList())
			));

		// 각 탭별로 제한된 개수 반환
		return MapTabViewResponse.of(
			wishlists,
			limit(groupedTabs.get(StoreType.PICK), 3),
			limit(groupedTabs.get(StoreType.TREND), 5),
			limit(groupedTabs.get(StoreType.TASTE), 5),
			limit(groupedTabs.get(StoreType.LAB), 5)
		);
	}

	// Store 데이터를 StoreTabInfo로 변환
	private StoreTabInfo convertToTabInfo(Store store) {
		return StoreTabInfo.of(store,
			store.getReviews().stream()
				.map(Reviews::getComment)
				.findFirst()
				.orElse(null),
			store.getStoreImages().stream()
				.map(StoreImage::getUrl)
				.findFirst()
				.orElse(null)
		);
	}

	// 리스트 최대 개수 제한
	private List<StoreTabInfo> limit(List<StoreTabInfo> stores, int limit) {
		return stores == null ? List.of() : stores.stream()
			.limit(limit)
			.collect(Collectors.toList());
	}

	public StoreSearchResponse searchStores(String keyword, String category, String type, int page,
		Principal principal) {
		Long userId = Long.parseLong(principal.getName());
		User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

		List<Store> stores = storeRepository.findAll();
		stores = filterByKeyword(stores, keyword);
		stores = filterByCategory(stores, category);
		stores = filterByType(stores, type, user);

		PageInfo pageInfo = PageInfo.builder()
			.currentPage(page)
			.totalPages((int)Math.ceil((double)stores.size() / PAGE_SIZE))
			.totalItems(stores.size())
			.build();

		List<StoreDetail> storeDetails = stores.stream()
			.skip((long)page * PAGE_SIZE)
			.limit(PAGE_SIZE)
			.map(store -> convertToStoreDetail(store, user))
			.toList();

		return StoreSearchResponse.of(pageInfo, storeDetails);
	}

	private List<Store> filterByKeyword(List<Store> stores, String keyword) {
		if (keyword == null || keyword.isBlank()) {
			return stores;
		}

		return stores.stream()
			.filter(store -> store.getName().contains(keyword) || store.getAddress().contains(keyword))
			.toList();
	}

	private List<Store> filterByCategory(List<Store> stores, String category) {
		if (category.equals("all")) {
			return stores;
		}

		try {
			return stores.stream()
				.filter(store -> store.getCategory().equals(StoreCategory.valueOf(category.toUpperCase())))
				.toList();
		} catch (IllegalArgumentException e) {
			throw new StoreNotFoundException("존재하지 않는 카테고리입니다.");
		}
	}

	private List<Store> filterByType(List<Store> stores, String type, User user) {
		return switch (type) {
			case "pick" -> stores.stream()
				.filter(store -> store.getType() == StoreType.PICK)
				.toList();
			case "trend" -> stores.stream()
				.filter(store -> store.getType() == StoreType.TREND)
				.toList();
			case "taste" -> stores.stream()
				.filter(store -> store.getType() == StoreType.TASTE)
				.toList();
			case "wish" -> stores.stream()
				.filter(store -> wishilistRepository.existsByUserAndStoreAndIsWishTrue(user, store))
				.toList();
			case "popular" -> storeRepository.findPopularStores();
			case "star" -> storeRepository.findTopRatedStores();
			case "near" -> stores;
			default -> throw new StoreNotFoundException("존재하지 않는 타입입니다.");
		};
	}

	private StoreDetail convertToStoreDetail(Store store, User user) {
		Double averageStars = store.getReviews().stream()
			.mapToDouble(Reviews::getStar)
			.average()
			.stream()
			.map(avg -> Math.round(avg * 10) / 10.0)
			.findFirst()
			.orElse(0.0);

		String topReview = store.getReviews().stream()
			.map(Reviews::getComment)
			.findFirst()
			.orElse(null);

		List<String> images = store.getStoreImages().stream()
			.map(StoreImage::getUrl)
			.toList();

		boolean isWish = wishilistRepository.findByUserAndStore(user, store)
			.map(Wishlist::isWish)
			.orElse(false);

		return StoreDetail.of(store, averageStars, isWish, topReview, images);
	}
}
