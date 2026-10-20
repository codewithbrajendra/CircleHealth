package com.circlesHealth.admin.utils;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtil {

	public static File saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws Exception {

		File imageFile = null;

		try {

			Path uploadPath = Paths.get(uploadDir);

			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}

			InputStream inputStream = multipartFile.getInputStream();

			Path filePath = uploadPath.resolve(fileName);
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);

			imageFile = filePath.toFile();

		} catch (Exception e) {
			throw e;
		}

		return imageFile;
	}
}
