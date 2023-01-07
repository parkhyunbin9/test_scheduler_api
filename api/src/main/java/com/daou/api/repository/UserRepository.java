package com.daou.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daou.api.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByUsernameAndPassword(String username, String password);

	Optional<User> findByUsername(String username);

	boolean existsByUsername(String username);
}
