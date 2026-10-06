package com.linkout.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.linkout.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	boolean existsByUsername(String username);
}
