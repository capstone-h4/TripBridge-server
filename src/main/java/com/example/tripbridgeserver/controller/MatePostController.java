package com.example.tripbridgeserver.controller;

import com.example.tripbridgeserver.dto.MatePostDTO;
import com.example.tripbridgeserver.entity.MatePost;
import com.example.tripbridgeserver.entity.UserEntity;
import com.example.tripbridgeserver.repository.MatePostRepository;
import com.example.tripbridgeserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

//Mate 게시판 관련 Controller
@RestController
public class MatePostController {

    private final MatePostRepository matePostRepository;
    private final UserRepository userRepository;

    @Autowired
    public MatePostController(MatePostRepository matePostRepository, UserRepository userRepository) {
        this.matePostRepository = matePostRepository;
        this.userRepository = userRepository;
    }

    //Mate 게시판 전체 조회
    @GetMapping("/mate")
    public List<MatePost> index(){
        return matePostRepository.findAllByOrderByCreatedAtDesc();
    }

    //Mate 게시판 단일 글 조회
    @GetMapping("/mate/{id}")
    public MatePost show (@PathVariable Long id){
        return matePostRepository.findById(id).orElse(null);
    }
    //Mate 게시판 글 생성
    @PostMapping("/mate")
    public MatePost create(@RequestBody  MatePostDTO dto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        UserEntity currentUser = userRepository.findByEmail(userEmail);
        MatePost matePost=dto.toEntity(currentUser);
        return matePostRepository.save(matePost);
    }

    //Mate 게시판 단일글 수정
    @PatchMapping("/mate/{id}")
    public ResponseEntity<MatePost> update(@PathVariable Long id, @RequestBody MatePostDTO dto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        UserEntity currentUser = userRepository.findByEmail(userEmail);
        MatePost matePost= dto.toEntity(currentUser);
        MatePost target = matePostRepository.findById(id).orElse(null);

        if(target==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        target.setTitle(matePost.getTitle());
        target.setContent(matePost.getContent());
        target.setUserEntity(matePost.getUserEntity());
        MatePost updated = matePostRepository.save(target);

        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }
    //Mate 게시판 단일글 삭제
    @DeleteMapping("/mate/{id}")
    public ResponseEntity<MatePost> delete(@PathVariable Long id){
        MatePost target = matePostRepository.findById(id).orElse(null);
        if(target==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        matePostRepository.delete(target);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
