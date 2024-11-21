package com.groom.swipo.domain.store.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.groom.swipo.domain.store.entity.Reviews;
import com.groom.swipo.domain.store.entity.Store;

@Repository
public interface ReviewsRepository extends JpaRepository<Reviews, Long> {
	List<Reviews> findAllByStore(Store store);
}
