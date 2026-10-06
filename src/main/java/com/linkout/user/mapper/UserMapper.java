package com.linkout.user.mapper;

import com.linkout.user.dtos.UserRequest;
import com.linkout.user.dtos.UserResponse;
import com.linkout.user.model.User;

public interface UserMapper {

	static User toEntity(UserRequest request) {
		User user = new User();
		user.setUsername(request.username().trim());
		return user;
	}

	static UserResponse toResponse(User user) {
		return new UserResponse(user.getId(), user.getUsername());
	}
}
