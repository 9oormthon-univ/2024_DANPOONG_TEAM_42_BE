package com.groom.swipo.domain.payment.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.groom.swipo.domain.payment.entity.Pay;
import com.groom.swipo.domain.user.entity.User;

@Repository
public interface PayRepository extends JpaRepository<Pay, Long> {
	Optional<Pay> findByUser(User user);
}
