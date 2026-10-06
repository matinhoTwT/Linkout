package com.linkout.post.dtos;

public record PostRequest(
		Long authorId,
		String content
) {
}
