package com.linkout.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkout.dto.PostCreateRequest;
import com.linkout.dto.PostResponse;
import com.linkout.dto.PostUpdateRequest;
import com.linkout.exception.ResourceNotFoundException;
import com.linkout.model.Post;
import com.linkout.model.User;
import com.linkout.repository.PostRepository;
import com.linkout.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {

	private final PostRepository postRepository;
	private final UserRepository userRepository;

	@Transactional
	public PostResponse create(PostCreateRequest request) {
		if (request.authorId() == null) {
			throw new IllegalArgumentException("authorId é obrigatório");
		}
		validateContent(request.content());

		User author = userRepository.findById(request.authorId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Usuário não encontrado: " + request.authorId()));

		Post post = new Post();
		post.setAuthor(author);
		post.setContent(request.content().trim());

		return toResponse(postRepository.save(post));
	}

	@Transactional(readOnly = true)
	public List<PostResponse> findAll() {
		return postRepository.findAllByOrderByCreateAtDesc().stream()
				.map(this::toResponse)
				.toList();
	}

	@Transactional(readOnly = true)
	public PostResponse findById(Long id) {
		return toResponse(getPost(id));
	}

	@Transactional
	public PostResponse update(Long id, PostUpdateRequest request) {
		validateContent(request.content());

		Post post = getPost(id);
		post.setContent(request.content().trim());

		// saveAndFlush para o @PreUpdate rodar antes de montar a resposta
		return toResponse(postRepository.saveAndFlush(post));
	}

	@Transactional
	public void delete(Long id) {
		postRepository.delete(getPost(id));
	}

	private Post getPost(Long id) {
		return postRepository.findWithAuthorById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Post não encontrado: " + id));
	}

	private void validateContent(String content) {
		if (content == null || content.isBlank()) {
			throw new IllegalArgumentException("content é obrigatório");
		}
	}

	private PostResponse toResponse(Post post) {
		return new PostResponse(
				post.getId(),
				post.getContent(),
				post.getImageUrl(),
				post.getAuthor().getId(),
				post.getAuthor().getUsername(),
				post.getCreateAt(),
				post.getUpdateAt());
	}
}
