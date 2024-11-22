package com.groom.swipo.domain.payment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.groom.swipo.domain.payment.entity.Pay;
import com.groom.swipo.domain.payment.entity.Paylist;

@Repository
public interface PaylistRepository extends JpaRepository<Paylist, Long> {
	List<Paylist> findTop5ByPayOrderByCreatedAtDesc(Pay pay);
}
