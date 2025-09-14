package com.viniciusmcabral.sound_rate.models;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "review_likes", uniqueConstraints = { @UniqueConstraint(columnNames = { "user_id", "album_review_id" }) })
public class ReviewLike {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "album_review_id", nullable = false)
	private AlbumReview albumReview;

	public ReviewLike() {
	}

	public ReviewLike(User user, AlbumReview albumReview) {
		this.user = user;
		this.albumReview = albumReview;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public AlbumReview getAlbumReview() {
		return albumReview;
	}

	public void setAlbumReview(AlbumReview albumReview) {
		this.albumReview = albumReview;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ReviewLike that = (ReviewLike) o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "ReviewLike [id=" + id + ", user=" + user + ", albumReview=" + albumReview + "]";
	}
}