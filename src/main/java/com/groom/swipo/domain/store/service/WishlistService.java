package com.groom.swipo.domain.store.service;

import java.security.Principal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.groom.swipo.domain.store.entity.Store;
import com.groom.swipo.domain.store.entity.Wishlist;
import com.groom.swipo.domain.store.exception.StoreNotFoundException;
import com.groom.swipo.domain.store.repository.StoreRepository;
import com.groom.swipo.domain.store.repository.WishilistRepository;
import com.groom.swipo.domain.user.entity.User;
import com.groom.swipo.domain.user.exception.UserNotFoundException;
import com.groom.swipo.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WishlistService {

	private final UserRepository userRepository;
	private final StoreRepository storeRepository;
	private final WishilistRepository wishilistRepository;

	@Transactional
	public void updateWish(Long storeId, boolean isWish, Principal principal) {
		Long userId = Long.parseLong(principal.getName());
		User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
		Store store = storeRepository.findById(storeId).orElseThrow(StoreNotFoundException::new);

		wishilistRepository.findByUserAndStore(user, store)
			.ifPresentOrElse(
				wishlist -> wishlist.updateIsWish(isWish),
				() -> wishilistRepository.save(
					Wishlist.builder()
						.user(user)
						.store(store)
						.isWish(isWish)
						.build()
				)
			);
	}
}
