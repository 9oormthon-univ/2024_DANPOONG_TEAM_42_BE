package com.groom.swipo.domain.store.service;

import java.security.Principal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.groom.swipo.domain.store.dto.request.ReviewsRegisterRequest;
import com.groom.swipo.domain.store.entity.Reviews;
import com.groom.swipo.domain.store.entity.Store;
import com.groom.swipo.domain.store.exception.StoreNotFoundException;
import com.groom.swipo.domain.store.repository.ReviewsRepository;
import com.groom.swipo.domain.store.repository.StoreRepository;
import com.groom.swipo.domain.user.entity.User;
import com.groom.swipo.domain.user.exception.UserNotFoundException;
import com.groom.swipo.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewsService {

	private final UserRepository userRepository;
	private final StoreRepository storeRepository;
	private final ReviewsRepository reviewsRepository;

	@Transactional
	public void registerReview(ReviewsRegisterRequest request, Principal principal) {
		Long userId = Long.parseLong(principal.getName());
		User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
		Store store = storeRepository.findById(request.storeId()).orElseThrow(StoreNotFoundException::new);

		Reviews review = request.toEntity(user, store);
		reviewsRepository.save(review);
	}
}
