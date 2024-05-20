package com.example.tripbridgeserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class TripPostDTO {
    private String title;
    private String content;
    private List<MultipartFile> images;
}
