package com.linkout.user.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.linkout.user.dtos.UserRequest;
import com.linkout.user.dtos.UserResponse;
import com.linkout.user.dtos.UserUpdateRequest;
import com.linkout.user.mapper.UserMapper;
import com.linkout.user.model.User;
import com.linkout.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	@Transactional
	public UserResponse create(UserRequest request) {
		validateUsername(request.username());
		checkUsernameAvailable(request.username().trim());

		User user = userRepository.save(UserMapper.toEntity(request));
		return UserMapper.toResponse(user);
	}

	@Transactional(readOnly = true)
	public List<UserResponse> findAll() {
		return userRepository.findAll().stream()
				.map(UserMapper::toResponse)
				.toList();
	}

	@Transactional(readOnly = true)
	public UserResponse findById(Long id) {
		return UserMapper.toResponse(getUser(id));
	}

	@Transactional
	public UserResponse update(Long id, UserUpdateRequest request) {
		validateUsername(request.username());

		User user = getUser(id);
		String username = request.username().trim();
		if (!username.equals(user.getUsername())) {
			checkUsernameAvailable(username);
			user.setUsername(username);
		}

		return UserMapper.toResponse(userRepository.save(user));
	}

	@Transactional
	public void delete(Long id) {
		userRepository.delete(getUser(id));
	}

	private User getUser(Long id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(
						HttpStatus.NOT_FOUND, "Usuário não encontrado: " + id));
	}

	private void validateUsername(String username) {
		if (username == null || username.isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "username é obrigatório");
		}
	}

	private void checkUsernameAvailable(String username) {
		if (userRepository.existsByUsername(username)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "username já está em uso");
		}
	}
}
