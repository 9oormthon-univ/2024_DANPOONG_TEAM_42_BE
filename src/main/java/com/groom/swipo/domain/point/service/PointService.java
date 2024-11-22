package com.groom.swipo.domain.point.service;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.groom.swipo.domain.payment.entity.Pay;
import com.groom.swipo.domain.payment.repository.PayRepository;
import com.groom.swipo.domain.point.dto.PieceInfo;
import com.groom.swipo.domain.point.dto.Request.SwipstoneSwapRequest;
import com.groom.swipo.domain.point.dto.Response.SwipstoneResponse;
import com.groom.swipo.domain.point.dto.Response.SwipstoneSwapResponse;
import com.groom.swipo.domain.point.entity.MyPiece;
import com.groom.swipo.domain.point.exception.PiecesNotFoundException;
import com.groom.swipo.domain.point.repository.MyPieceRepository;
import com.groom.swipo.domain.user.entity.User;
import com.groom.swipo.domain.user.exception.UserNotFoundException;
import com.groom.swipo.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PointService {
	private final UserRepository userRepository;
	private final MyPieceRepository myPieceRepository;
	private final PayRepository payRepository;

	public SwipstoneResponse getSwipstone(Principal principal) {
		Long userId = Long.parseLong(principal.getName());
		User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

		// isDeleted가 false인 조각들만 조회
		List<MyPiece> myPieces = myPieceRepository.findAllByUserAndIsDeletedFalse(user);

		if (myPieces.isEmpty()) {
			throw new PiecesNotFoundException();
		}

		List<PieceInfo> pieces = myPieces.stream()
			.map(myPiece -> PieceInfo.from(myPiece, myPiece.getPiece()))
			.toList();

		Integer piecesNum = pieces.size();
		return SwipstoneResponse.of(piecesNum, pieces);
	}

	@Transactional
	public SwipstoneSwapResponse swapSwipstone(SwipstoneSwapRequest request, Principal principal) {
		Long userId = Long.parseLong(principal.getName());
		User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

		List<MyPiece> myPieces = myPieceRepository.findAllByIdInAndUserId(request.myPieceIds(), userId);

		// 요청된 조각 수와 조회된 조각 수가 같지 않거나, 삭제된 조각이 포함된 경우 예외 발생
		if (myPieces.size() != request.myPieceIds().size() || myPieces.stream().anyMatch(MyPiece::getIsDeleted)) {
			throw new PiecesNotFoundException("보유한 조각이 부족하거나 사용 불가능한 조각이 포함되어 있습니다.");
		}

		// 페이 돈 업데이트
		Pay pay = user.getPay();
		pay.updatePay(request.point()); // save 안해도 반영됨

		// 조각 상태변경
		myPieces.forEach(myPiece -> {
			myPiece.setIsDeleted(true); // save 안해도 반영됨
		});

		return new SwipstoneSwapResponse(pay.getTotalPay());
	}
}