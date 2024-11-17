package com.groom.swipo.domain.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.groom.swipo.domain.payment.entity.PayChargeList;

@Repository
public interface PayChargeListRepository extends JpaRepository<PayChargeList, Long> {
}
