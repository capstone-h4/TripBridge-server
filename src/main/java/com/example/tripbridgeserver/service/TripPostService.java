package com.example.tripbridgeserver.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.example.tripbridgeserver.dto.TripPostRequest;
import com.example.tripbridgeserver.entity.TripImage;
import com.example.tripbridgeserver.entity.TripPost;
import com.example.tripbridgeserver.entity.User;
import com.example.tripbridgeserver.repository.TripPostRepository;
import com.example.tripbridgeserver.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class TripPostService {

    private final AmazonS3 amazonS3Client;
    private final TripPostRepository tripPostRepository;
    private final UserRepository userRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public TripPost createTripPost(TripPostRequest tripPostRequest, String userEmail) {
        User user = userRepository.findByEmail(userEmail);

        if (tripPostRequest.getImages() == null) {
            tripPostRequest.setImages(new ArrayList<>()); // 이미지 목록을 빈 리스트로 설정
        }
        TripPost tripPost= toEntity(tripPostRequest, user);
        return tripPostRepository.save(tripPost);
    }

    public void deleteTripPost(Long id) {
        TripPost tripPost = tripPostRepository.findById(id).orElse(null);
        if(tripPost == null){
            return;
        }

        deleteImageFromS3(tripPost.getImages());

        tripPostRepository.delete(tripPost);
    }

    public TripPost toEntity(TripPostRequest tripPostRequest, User user) {
        TripPost tripPost = new TripPost();
        tripPost.setTitle(tripPostRequest.getTitle());
        tripPost.setContent(tripPostRequest.getContent());
        tripPost.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        tripPost.setUser(user);

        List<TripImage> tripImages = new ArrayList<>();

        for (MultipartFile imageFile : tripPostRequest.getImages()) {
            TripImage tripImage = new TripImage();
            String imageUrl = uploadImageToS3(imageFile);
            tripImage.setImageUrl(imageUrl);
            tripImage.setTripPost(tripPost);
            tripImages.add(tripImage);
        }
        tripPost.setImages(tripImages);

        return tripPost;
    }

    // S3 버킷에 업로드 시, 이미지 이름 랜덤 생성
    private String generateRandomImageName(String originName) {
        String random = UUID.randomUUID().toString();
        originName = originName.replace(" ", "%20");
        return random + originName;
    }

    private String uploadImageToS3(MultipartFile image) { // 버킷에 이미지 업로드
        String originName = image.getOriginalFilename();
        String ext = originName.substring(originName.lastIndexOf("."));
        String changedName = generateRandomImageName(originName);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/" + ext);
        metadata.setContentLength(image.getSize());
        try {
            amazonS3Client.putObject(new PutObjectRequest(
                    bucketName, changedName, image.getInputStream(), metadata
            ).withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드에 실패했습니다.");
        }
        return amazonS3Client.getUrl(bucketName, changedName).toString();
    }

    public void deleteImageFromS3(List<TripImage> tripImages) { // 버킷 이미지 삭제
        for (TripImage tripImage : tripImages) {
            String imageUrl = tripImage.getImageUrl();
            // 객체 Key 추출
            String objectKey = getObjectKeyFromImageUrl(imageUrl);
            // 버킷에서 이미지 삭제
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucketName, objectKey));
        }
    }

    private String getObjectKeyFromImageUrl(String imageUrl) { // 버킷에서 이미지 삭제 시, 객체 Key 추출
        String bucketEndMarker = ".com/";
        int bucketEndIndex = imageUrl.indexOf(bucketEndMarker) + bucketEndMarker.length();
        String objectKey = imageUrl.substring(bucketEndIndex);
        log.info("Extracted object key from URL: {}", objectKey);

        return objectKey;
    }
}



