package com.viniciusmcabral.sound_rate.models;

import java.util.Objects;

import jakarta.persistence.*;

@Entity
@Table(name = "album_likes", uniqueConstraints = { @UniqueConstraint(columnNames = { "user_id", "albumId" }) })
public class AlbumLike {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(nullable = false)
	private String albumId;

	public AlbumLike() {
	}

	public AlbumLike(User user, String albumId) {
		this.user = user;
		this.albumId = albumId;
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

	public String getAlbumId() {
		return albumId;
	}

	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(albumId, id, user);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AlbumLike other = (AlbumLike) obj;
		return Objects.equals(albumId, other.albumId) && Objects.equals(id, other.id)
				&& Objects.equals(user, other.user);
	}

	@Override
	public String toString() {
		return "AlbumLike [id=" + id + ", user=" + user + ", albumId=" + albumId + "]";
	}
}