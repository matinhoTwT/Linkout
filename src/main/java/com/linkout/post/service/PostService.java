package com.linkout.post.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.linkout.post.dtos.PostRequest;
import com.linkout.post.dtos.PostResponse;
import com.linkout.post.dtos.PostUpdateRequest;
import com.linkout.post.mapper.PostMapper;
import com.linkout.post.model.Post;
import com.linkout.post.repository.PostRepository;
import com.linkout.user.model.User;
import com.linkout.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {

	private final PostRepository postRepository;
	private final UserRepository userRepository;

	@Transactional
	public PostResponse create(PostRequest request) {
		if (request.authorId() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "authorId é obrigatório");
		}
		validateContent(request.content());

		User author = userRepository.findById(request.authorId())
				.orElseThrow(() -> new ResponseStatusException(
						HttpStatus.NOT_FOUND, "Usuário não encontrado: " + request.authorId()));

		Post post = postRepository.save(PostMapper.toEntity(request, author));
		return PostMapper.toResponse(post);
	}

	@Transactional(readOnly = true)
	public List<PostResponse> findAll() {
		return postRepository.findAllByOrderByCreateAtDesc().stream()
				.map(PostMapper::toResponse)
				.toList();
	}

	@Transactional(readOnly = true)
	public PostResponse findById(Long id) {
		return PostMapper.toResponse(getPost(id));
	}

	@Transactional
	public PostResponse update(Long id, PostUpdateRequest request) {
		validateContent(request.content());

		Post post = getPost(id);
		post.setContent(request.content().trim());

		// saveAndFlush para o @PreUpdate rodar antes de montar a resposta
		return PostMapper.toResponse(postRepository.saveAndFlush(post));
	}

	@Transactional
	public void delete(Long id) {
		postRepository.delete(getPost(id));
	}

	private Post getPost(Long id) {
		return postRepository.findWithAuthorById(id)
				.orElseThrow(() -> new ResponseStatusException(
						HttpStatus.NOT_FOUND, "Post não encontrado: " + id));
	}

	private void validateContent(String content) {
		if (content == null || content.isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "content é obrigatório");
		}
	}
}
