package com.viniciusmcabral.sound_rate.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
public class StorageService {

	private final Cloudinary cloudinary;

	public StorageService(Cloudinary cloudinary) {
		this.cloudinary = cloudinary;
	}

	public String uploadFile(MultipartFile file) {
		try {
			String publicId = "avatars/" + UUID.randomUUID().toString();

			Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(),
					ObjectUtils.asMap("public_id", publicId, "overwrite", true));

			return (String) uploadResult.get("secure_url");
		} catch (IOException e) {
			throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
		}
	}
}