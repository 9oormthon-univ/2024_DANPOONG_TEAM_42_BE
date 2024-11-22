package com.groom.swipo.domain.point.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.groom.swipo.domain.point.dto.PieceInfo;
import com.groom.swipo.domain.point.entity.MyPiece;
import com.groom.swipo.domain.user.entity.User;

@Repository
public interface MyPieceRepository extends JpaRepository<MyPiece, Long> {
	List<MyPiece> findAllByUserAndIsDeletedFalse(User user);
	List<MyPiece> findAllByIdInAndUserId(List<Long> ids, Long userId);
}
