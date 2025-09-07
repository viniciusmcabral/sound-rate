package com.viniciusmcabral.sound_rate.services;

import java.io.IOException;
import java.time.LocalDate;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.viniciusmcabral.sound_rate.models.CriticReview;
import com.viniciusmcabral.sound_rate.repositories.CriticReviewRepository;

@Service
public class ScraperService {
	
	private final CriticReviewRepository criticReviewRepository;

	public ScraperService(CriticReviewRepository criticReviewRepository) {
		this.criticReviewRepository = criticReviewRepository;
	}

	@Async 
	public void fetchAndSavePitchforkScore(String artistName, String albumName, String albumId) {

		if (criticReviewRepository.findByAlbumId(albumId).isPresent()) {
			return; 
		}

		try {
			String formattedArtist = formatForUrl(artistName);
			String formattedAlbum = formatForUrl(albumName);
			String url = "https://pitchfork.com/reviews/albums/" + formattedArtist + "-" + formattedAlbum + "/";

			Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36").get();

			Elements scoreElement = doc.select("p[class*='Score-']"); 

			if (!scoreElement.isEmpty()) {
				String scoreText = scoreElement.first().text();
				double score = Double.parseDouble(scoreText);

				CriticReview criticReview = new CriticReview();
				criticReview.setAlbumId(albumId);
				criticReview.setSource("Pitchfork");
				criticReview.setScore(score);
				criticReview.setReviewUrl(url);
				criticReview.setLastCheckedAt(LocalDate.now());

				criticReviewRepository.save(criticReview);
				System.out.println("SUCCESS: Scraped score " + score + " for album " + albumName);
			}

		} catch (IOException | NumberFormatException e) {
			System.err.println("FAILED to scrape for album " + albumName + ". Reason: " + e.getMessage());
		}
	}

	private String formatForUrl(String text) {
		return text.toLowerCase().replaceAll("\\s+", "-") .replaceAll("[^a-z0-9-]", ""); 
	}
}
