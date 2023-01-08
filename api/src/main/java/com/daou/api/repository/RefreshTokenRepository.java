package com.daou.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daou.api.model.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
	Optional<RefreshToken> findByKey(String key);
}
