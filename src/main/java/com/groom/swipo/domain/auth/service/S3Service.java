package com.groom.swipo.domain.auth.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class S3Service {

	private final AmazonS3 amazonS3;

	@Value("${cloud.s3.bucket}")
	private String bucketName;

	public S3Service(AmazonS3 amazonS3) {
		this.amazonS3 = amazonS3;
	}

	public String uploadImage(MultipartFile image) {
		String fileName = UUID.randomUUID().toString() + "-" + image.getOriginalFilename();
		try (InputStream inputStream = image.getInputStream()) {
			ObjectMetadata metadata = new ObjectMetadata(); // 콘텐츠 길이 지정
			metadata.setContentLength(image.getSize());

			amazonS3.putObject(new PutObjectRequest(bucketName, fileName, inputStream, metadata));
			return amazonS3.getUrl(bucketName, fileName).toString();
		} catch (IOException e) {
			throw new RuntimeException("이미지 업로드 중 오류가 발생했습니다.", e);
		}
	}
}
