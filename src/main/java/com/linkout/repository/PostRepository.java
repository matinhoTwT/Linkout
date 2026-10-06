package com.linkout.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.linkout.model.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

	@EntityGraph(attributePaths = "author")
	List<Post> findAllByOrderByCreateAtDesc();

	@EntityGraph(attributePaths = "author")
	Optional<Post> findWithAuthorById(Long id);
}
