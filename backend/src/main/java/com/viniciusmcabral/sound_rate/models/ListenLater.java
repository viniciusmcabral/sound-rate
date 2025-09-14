package com.viniciusmcabral.sound_rate.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "listen_later_entries", uniqueConstraints = { @UniqueConstraint(columnNames = { "user_id", "albumId" }) })
public class ListenLater {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(nullable = false)
	private String albumId;

	@Column(nullable = false)
	private LocalDateTime addedAt;

	@PrePersist
	protected void onAdd() {
		this.addedAt = LocalDateTime.now();
	}

	public ListenLater() {
	}

	public ListenLater(User user, String albumId) {
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

	public LocalDateTime getAddedAt() {
		return addedAt;
	}

	public void setAddedAt(LocalDateTime addedAt) {
		this.addedAt = addedAt;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ListenLater that = (ListenLater) o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "ListenLater [id=" + id + ", user=" + user + ", albumId=" + albumId + ", addedAt=" + addedAt + "]";
	}
}