package com.groom.swipo.domain.store.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.groom.swipo.domain.store.entity.Store;
import com.groom.swipo.domain.store.entity.StoreImage;

@Repository
public interface StoreImageRepository extends JpaRepository<StoreImage, Long> {
	List<StoreImage> findAllByStore(Store store);
}
