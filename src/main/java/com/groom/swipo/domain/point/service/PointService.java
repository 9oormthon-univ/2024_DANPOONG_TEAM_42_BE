package com.groom.swipo.domain.point.service;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.groom.swipo.domain.point.dto.PieceInfo;
import com.groom.swipo.domain.point.dto.Request.SwipstoneSwapResquest;
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

	public SwipstoneResponse getSwipstone(Principal principal) {
		Long userId = Long.parseLong(principal.getName());
		User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

		List<MyPiece> myPieces = myPieceRepository.findAllByUser(user);

		if (myPieces.isEmpty()) {
			throw new PiecesNotFoundException();
		}

		List<PieceInfo> pieces = myPieces.stream()
			.map(myPiece -> PieceInfo.from(myPiece ,myPiece.getPiece()))
			.toList();

		Integer piecesNum = pieces.size();
		return SwipstoneResponse.of(piecesNum, pieces);
	}
}