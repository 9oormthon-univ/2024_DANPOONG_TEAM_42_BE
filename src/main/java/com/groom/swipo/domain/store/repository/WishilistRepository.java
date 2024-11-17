package com.groom.swipo.domain.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.groom.swipo.domain.store.entity.Wishlist;

@Repository
public interface WishilistRepository extends JpaRepository<Wishlist, Long> {
}
