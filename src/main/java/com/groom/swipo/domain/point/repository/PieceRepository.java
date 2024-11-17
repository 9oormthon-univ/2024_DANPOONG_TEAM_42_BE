package com.groom.swipo.domain.point.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.groom.swipo.domain.point.entity.Piece;

@Repository
public interface PieceRepository extends JpaRepository<Piece, Long> {
}
