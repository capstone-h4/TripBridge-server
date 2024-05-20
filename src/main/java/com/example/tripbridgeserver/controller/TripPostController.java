package com.example.tripbridgeserver.controller;


import com.example.tripbridgeserver.dto.TripPostDTO;
import com.example.tripbridgeserver.entity.TripPost;
import com.example.tripbridgeserver.entity.UserEntity;
import com.example.tripbridgeserver.repository.TripPostRepository;
import com.example.tripbridgeserver.repository.UserRepository;
import com.example.tripbridgeserver.service.TripPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

//Trip 게시판 관련 Controller
@RestController
public class TripPostController {

    private final TripPostService tripPostService;
    private final TripPostRepository tripPostRepository;
    private final UserRepository userRepository;

    @Autowired
    public TripPostController(TripPostService tripPostService,TripPostRepository tripPostRepository, UserRepository userRepository) {
        this.tripPostRepository = tripPostRepository;
        this.userRepository = userRepository;
        this.tripPostService = tripPostService;
    }
    //Trip 게시판 전체 조회
    @GetMapping("/trip")
    public List<TripPost> index(){
        return tripPostRepository.findAllByOrderByCreatedAtDesc();
    }
    //Trip 게시판 단일 글 조회
    @GetMapping("/trip/{id}")
    public TripPost show (@PathVariable Long id){
        return tripPostRepository.findById(id).orElse(null);
    }
    //Trip 게시판 글 생성
    @PostMapping("/trip")
    public TripPost create(@ModelAttribute TripPostDTO dto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        UserEntity currentUser = userRepository.findByEmail(userEmail);

        if (dto.getImages() == null) {
            dto.setImages(new ArrayList<>()); // 이미지 목록을 빈 리스트로 설정
        }
        TripPost tripPost= tripPostService.toEntity(dto,currentUser);
        return tripPostRepository.save(tripPost);
    }
    //Trip 게시판 단일글 삭제
    @DeleteMapping("/trip/{id}")
    public ResponseEntity<TripPost> delete(@PathVariable Long id){
        TripPost target = tripPostRepository.findById(id).orElse(null);
        if(target==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        tripPostService.deleteImageFromS3(target.getImages());
        tripPostRepository.delete(target);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}

