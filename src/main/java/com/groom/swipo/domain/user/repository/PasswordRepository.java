package com.groom.swipo.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.groom.swipo.domain.user.entity.Password;

@Repository
public interface PasswordRepository extends JpaRepository<Password, String> {
}