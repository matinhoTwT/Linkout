package com.linkout.post.mapper;

import com.linkout.post.dtos.PostRequest;
import com.linkout.post.dtos.PostResponse;
import com.linkout.post.model.Post;
import com.linkout.user.model.User;

public interface PostMapper {

	static Post toEntity(PostRequest request, User author) {
		Post post = new Post();
		post.setAuthor(author);
		post.setContent(request.content().trim());
		return post;
	}

	static PostResponse toResponse(Post post) {
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
