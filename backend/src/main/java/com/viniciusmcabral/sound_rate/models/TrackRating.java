package com.viniciusmcabral.sound_rate.models;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "track_ratings", uniqueConstraints = { @UniqueConstraint(columnNames = { "user_id", "trackId" }) })
public class TrackRating {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String albumId;

	@Column(nullable = false)
	private String trackId;

	@Column(nullable = false)
	private Integer rating;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	public TrackRating() {
	}

	public TrackRating(String albumId, String trackId, Integer rating, User user) {
		this.albumId = albumId;
		this.trackId = trackId;
		this.rating = rating;
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAlbumId() {
		return albumId;
	}

	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}

	public String getTrackId() {
		return trackId;
	}

	public void setTrackId(String trackId) {
		this.trackId = trackId;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		TrackRating that = (TrackRating) o;
		return id != null && Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

	@Override
	public String toString() {
		return "TrackRating{" + "id=" + id + ", albumId='" + albumId + '\'' + ", trackId='" + trackId + '\''
				+ ", rating=" + rating + ", userId=" + (user != null ? user.getId() : null) + '}';
	}
}