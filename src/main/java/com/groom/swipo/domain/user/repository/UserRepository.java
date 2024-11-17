package com.groom.swipo.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.groom.swipo.domain.user.entity.User;
import com.groom.swipo.domain.user.entity.enums.Provider;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
