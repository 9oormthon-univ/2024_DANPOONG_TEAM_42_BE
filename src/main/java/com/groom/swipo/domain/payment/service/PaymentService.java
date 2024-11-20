package com.groom.swipo.domain.payment.service;

import java.security.Principal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.groom.swipo.domain.payment.dto.request.PaymentCompleteRequest;
import com.groom.swipo.domain.payment.dto.response.PaymentCompleteResponse;
import com.groom.swipo.domain.payment.dto.response.PaymentPageResponse;
import com.groom.swipo.domain.payment.exception.PasswordMismatchException;
import com.groom.swipo.domain.point.entity.Card;
import com.groom.swipo.domain.point.exception.CardNotFoundException;
import com.groom.swipo.domain.point.exception.InsufficientPointsException;
import com.groom.swipo.domain.store.entity.Store;
import com.groom.swipo.domain.store.exception.StoreNotFoundException;
import com.groom.swipo.domain.store.repository.StoreRepository;
import com.groom.swipo.domain.user.entity.User;
import com.groom.swipo.domain.user.exception.UserNotFoundException;
import com.groom.swipo.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {

	private final UserRepository userRepository;
	private final StoreRepository storeRepository;

	public PaymentPageResponse getPaymentPage(Long storeId, Principal principal) {
		Long userId = Long.parseLong(principal.getName());
		User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

		Store store = storeRepository.findById(storeId).orElseThrow(StoreNotFoundException::new);
		Card card = user.getCards().stream()
			.filter(c -> c.getArea().equals(store.getArea()))
			.findFirst()
			.orElseThrow(CardNotFoundException::new);

		return PaymentPageResponse.of(card, user, store);
	}

	@Transactional
	public PaymentCompleteResponse completePayment(PaymentCompleteRequest request, Principal principal) {
		Long userId = Long.parseLong(principal.getName());
		User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

		Store store = storeRepository.findById(request.storeId()).orElseThrow(StoreNotFoundException::new);
		Card card = user.getCards().stream()
			.filter(c -> c.getArea().equals(store.getArea()))
			.findFirst()
			.orElseThrow(CardNotFoundException::new);

		if (!user.getPassword().equals(request.password())) {
			throw new PasswordMismatchException();
		}

		if (request.usedPoints() > card.getTotalPoint()) {
			throw new InsufficientPointsException();
		}

		Integer netPayment = request.amount() - request.usedPoints();
		Integer earnedPoints = (int)(netPayment * store.getPercent() / 100.0);

		user.getPay().updatePay(-netPayment);
		card.updatePoint(earnedPoints - request.usedPoints());

		return PaymentCompleteResponse.of(request.amount(), earnedPoints, user.getPay().getTotalPay());
	}
}
