package com.example.tripbridgeserver.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.example.tripbridgeserver.config.S3Config;
import com.example.tripbridgeserver.dto.TripPostDTO;
import com.example.tripbridgeserver.entity.TripImage;
import com.example.tripbridgeserver.entity.TripPost;
import com.example.tripbridgeserver.entity.UserEntity;
import com.example.tripbridgeserver.repository.TripPostRepository;
import com.example.tripbridgeserver.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class TripPostService {

    private final AmazonS3 amazonS3Client;
    private final TripPostRepository tripPostRepository;
    private final UserRepository userRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Autowired
    public TripPostService(AmazonS3 amazonS3Client, TripPostRepository tripPostRepository, UserRepository userRepository, S3Config s3Config) {
        this.amazonS3Client = amazonS3Client;
        this.tripPostRepository = tripPostRepository;
        this.userRepository = userRepository;
    }

    //Trip 게시글 Entity 로 변환
    public TripPost toEntity(TripPostDTO dto, UserEntity currentUser) {
        TripPost tripPost = new TripPost();
        tripPost.setTitle(dto.getTitle());
        tripPost.setContent(dto.getContent());
        tripPost.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        tripPost.setUserEntity(currentUser);

        List<TripImage> tripImages = new ArrayList<>();

        for (MultipartFile imageFile : dto.getImages()) {
            TripImage tripImage = new TripImage();
            String imageUrl = uploadImageToS3(imageFile);
            tripImage.setImageUrl(imageUrl);
            tripImage.setTripPost(tripPost);
            tripImages.add(tripImage);
        }
        tripPost.setImages(tripImages);

        return tripPost;
    }

    //S3 버킷에 업로들할 이미지의 이름 랜덤 생성
    private String generateRandomImageName(String originName) {
        String random = UUID.randomUUID().toString();
        originName = originName.replace(" ", "%20");
        return random + originName;
    }
    //S3 버킷에 이미지 업로드
    private String uploadImageToS3(MultipartFile image) {
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

    //S3 버킷에서 이미지 삭제
    public void deleteImageFromS3(List<TripImage> tripImages) {
        for (TripImage tripImage : tripImages) {
            String imageUrl = tripImage.getImageUrl();
            // S3 객체의 키(Key)를 추출
            String objectKey = getObjectKeyFromImageUrl(imageUrl);
            // S3 버킷에서 이미지 삭제
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucketName, objectKey));
        }
    }
    //S3 버킷에서 이미지 삭제할때 객체의 키 추출
    private String getObjectKeyFromImageUrl(String imageUrl) {
        String bucketEndMarker = ".com/";
        int bucketEndIndex = imageUrl.indexOf(bucketEndMarker) + bucketEndMarker.length();
        String objectKey = imageUrl.substring(bucketEndIndex);
        log.info("Extracted object key from URL: {}", objectKey);

        return objectKey;
    }
}



