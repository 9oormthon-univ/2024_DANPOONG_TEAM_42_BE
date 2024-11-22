package com.groom.swipo.domain.point.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.groom.swipo.domain.point.entity.Card;
import com.groom.swipo.domain.user.entity.User;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
	List<Card> findAllByUser(User user);
}
