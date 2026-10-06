package com.linkout.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.linkout.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
