package com.linkout.post.dtos;

import java.time.LocalDateTime;

public record PostResponse(
		Long id,
		String content,
		String imageUrl,
		Long authorId,
		String authorUsername,
		LocalDateTime createAt,
		LocalDateTime updateAt
) {
}
