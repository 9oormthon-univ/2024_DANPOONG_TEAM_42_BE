package com.groom.swipo.domain.store.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.groom.swipo.domain.store.entity.Store;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

	// 구면 코사인 법칙
	@Query("""
		    SELECT s FROM Store s
		    WHERE (
		        6371 * acos(
		            cos(radians(:latitude)) * cos(radians(s.latitude))
		            * cos(radians(s.longitude) - radians(:longitude))
		            + sin(radians(:latitude)) * sin(radians(s.latitude))
		        )
		    ) <= :radius
		""")
	List<Store> findStoresWithinRadius(
		@Param("latitude") double latitude,
		@Param("longitude") double longitude,
		@Param("radius") double radius
	);
}
