package com.groom.swipo.domain.point.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.groom.swipo.domain.point.entity.Cardlist;

@Repository
public interface CardlistRepository extends JpaRepository<Cardlist, Long> {
}
