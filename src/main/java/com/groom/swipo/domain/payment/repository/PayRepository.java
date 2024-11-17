package com.groom.swipo.domain.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.groom.swipo.domain.payment.entity.Pay;

@Repository
public interface PayRepository extends JpaRepository<Pay, Long> {
}