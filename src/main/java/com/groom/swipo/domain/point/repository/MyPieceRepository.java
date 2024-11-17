package com.groom.swipo.domain.point.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.groom.swipo.domain.point.entity.MyPiece;

@Repository
public interface MyPieceRepository extends JpaRepository<MyPiece, Long> {
}
