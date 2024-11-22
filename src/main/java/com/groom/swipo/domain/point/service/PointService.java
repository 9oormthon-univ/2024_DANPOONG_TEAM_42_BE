package com.groom.swipo.domain.point.service;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.groom.swipo.domain.auth.service.S3Service;
import com.groom.swipo.domain.payment.dto.PaylistInfo;
import com.groom.swipo.domain.payment.entity.Pay;
import com.groom.swipo.domain.payment.entity.Paylist;
import com.groom.swipo.domain.payment.repository.PayRepository;
import com.groom.swipo.domain.payment.repository.PaylistRepository;
import com.groom.swipo.domain.point.dto.PieceInfo;
import com.groom.swipo.domain.point.dto.Request.SwipstoneSwapRequest;
import com.groom.swipo.domain.point.dto.Response.PointHomeResponse;
import com.groom.swipo.domain.point.dto.Response.SwipstoneResponse;
import com.groom.swipo.domain.point.dto.Response.SwipstoneSwapResponse;
import com.groom.swipo.domain.point.entity.Card;
import com.groom.swipo.domain.point.entity.MyPiece;
import com.groom.swipo.domain.point.exception.DuplicateCardException;
import com.groom.swipo.domain.point.exception.PiecesNotFoundException;
import com.groom.swipo.domain.point.repository.CardRepository;
import com.groom.swipo.domain.point.repository.MyPieceRepository;
import com.groom.swipo.domain.point.dto.CardInfo;
import com.groom.swipo.domain.user.entity.User;
import com.groom.swipo.domain.user.exception.UserNotFoundException;
import com.groom.swipo.domain.payment.exception.PayNotFoundException;
import com.groom.swipo.domain.user.repository.UserRepository;
import com.groom.swipo.global.common.enums.Area;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PointService {
	private final UserRepository userRepository;
	private final MyPieceRepository myPieceRepository;
	private final PayRepository payRepository;
	private final CardRepository cardRepository;
	private final PaylistRepository paylistRepository;
	private final S3Service s3Service;

	public PointHomeResponse getHome(Principal principal) {
		Long userId = Long.parseLong(principal.getName());
		User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

		// 사용자 Pay 정보 조회
		Pay pay = payRepository.findByUser(user).orElseThrow(PayNotFoundException::new);

		// 사용자 카드 정보 조회
		List<Card> cards = cardRepository.findAllByUser(user);
		List<CardInfo> cardInfos = cards.stream()
			.map(CardInfo::from)
			.toList();

		// 최근 페이 거래 내역 조회 (최대 5개)
		List<Paylist> recentPaylists = paylistRepository.findTop5ByPayOrderByCreatedAtDesc(pay);
		List<PaylistInfo> paylistInfos = recentPaylists.stream()
			.map(paylist -> PaylistInfo.of(paylist, paylist.getStore()))
			.toList();

		return PointHomeResponse.of(pay, cardInfos.size(), cardInfos, paylistInfos);
	}

	@Transactional
	public void registerCard(String region, MultipartFile customImage, Principal principal) {
		Long userId = Long.parseLong(principal.getName());
		User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

		// 지역 확인
		Area area = Area.fromRegionName(region);

		// 중복 카드 확인
		if (cardRepository.existsByUserAndArea(user, area)) {
			throw new DuplicateCardException();
		}

		// 이미지 저장 로직
		String imageUrl = null;
		if (customImage != null && !customImage.isEmpty()) {
			imageUrl = s3Service.uploadImage(customImage);
		}

		// 카드 등록
		Card card = Card.builder()
			.user(user)
			.area(area)
			.customImage(imageUrl != null ? imageUrl : "default") // 없으면 default
			.build();
		cardRepository.save(card);
	}

	public SwipstoneResponse getSwipstone(Principal principal) {
		Long userId = Long.parseLong(principal.getName());
		User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

		// isDeleted가 false인 조각들만 조회
		List<MyPiece> myPieces = myPieceRepository.findAllByUserAndIsDeletedFalse(user);

		if (myPieces.isEmpty()) {
			throw new PiecesNotFoundException();
		}

		List<PieceInfo> pieces = myPieces.stream()
			.map(myPiece -> PieceInfo.of(myPiece, myPiece.getPiece()))
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
