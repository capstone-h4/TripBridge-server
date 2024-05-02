package com.example.tripbridgeserver.dto;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.tripbridgeserver.entity.TripImage;
import com.example.tripbridgeserver.entity.TripPost;
import com.example.tripbridgeserver.entity.UserEntity;
import com.example.tripbridgeserver.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class TripPostDTO {
    private String title;
    private String content;
    private List<MultipartFile> images;
}
