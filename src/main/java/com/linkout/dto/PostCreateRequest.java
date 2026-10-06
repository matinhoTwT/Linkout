package com.linkout.dto;

public record PostCreateRequest(
		Long authorId,
		String content
) {
}
