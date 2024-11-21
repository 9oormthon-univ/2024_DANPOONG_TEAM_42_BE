package com.groom.swipo.domain.store.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.groom.swipo.domain.store.entity.Store;
import com.groom.swipo.domain.store.entity.Wishlist;
import com.groom.swipo.domain.user.entity.User;

@Repository
public interface WishilistRepository extends JpaRepository<Wishlist, Long> {
	Optional<Wishlist> findByUserAndStore(User user, Store store);
	boolean existsByUserAndStoreAndIsWishTrue(User user, Store store);
}
