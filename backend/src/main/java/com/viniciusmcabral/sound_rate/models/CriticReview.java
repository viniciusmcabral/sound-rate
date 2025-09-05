package com.viniciusmcabral.sound_rate.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "critic_reviews")
public class CriticReview {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String albumId;

	@Column(nullable = false)
	private String source;

	@Column(nullable = false)
	private Double score;

	private String reviewUrl;

	private LocalDate lastCheckedAt;

	public CriticReview() {
	}

	public CriticReview(String albumId, String source, Double score, String reviewUrl, LocalDate lastCheckedAt) {
		this.albumId = albumId;
		this.source = source;
		this.score = score;
		this.reviewUrl = reviewUrl;
		this.lastCheckedAt = lastCheckedAt;
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

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public String getReviewUrl() {
		return reviewUrl;
	}

	public void setReviewUrl(String reviewUrl) {
		this.reviewUrl = reviewUrl;
	}

	public LocalDate getLastCheckedAt() {
		return lastCheckedAt;
	}

	public void setLastCheckedAt(LocalDate lastCheckedAt) {
		this.lastCheckedAt = lastCheckedAt;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		CriticReview that = (CriticReview) o;
		return id != null && Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

	@Override
	public String toString() {
		return "CriticReview{" + "id=" + id + ", albumId='" + albumId + '\'' + ", source='" + source + '\'' + ", score="
				+ score + ", reviewUrl='" + reviewUrl + '\'' + ", lastCheckedAt=" + lastCheckedAt + '}';
	}
}