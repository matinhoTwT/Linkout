package com.linkout.post.model;

import java.time.LocalDateTime;

import com.linkout.user.model.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String content;

	@Column(name = "image_url")
	private String imageUrl;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "author_id", nullable = false)
	private User author;

	@Column(name = "create_at", nullable = false, updatable = false)
	private LocalDateTime createAt;

	@Column(name = "update_at", nullable = false)
	private LocalDateTime updateAt;

	@PrePersist
	void onCreate() {
		LocalDateTime now = LocalDateTime.now();
		this.createAt = now;
		this.updateAt = now;
	}

	@PreUpdate
	void onUpdate() {
		this.updateAt = LocalDateTime.now();
	}
}
