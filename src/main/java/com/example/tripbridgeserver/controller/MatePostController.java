package com.example.tripbridgeserver.controller;

import com.example.tripbridgeserver.dto.MatePostRequest;
import com.example.tripbridgeserver.entity.MatePost;
import com.example.tripbridgeserver.repository.MatePostRepository;
import com.example.tripbridgeserver.service.MatePostService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MatePostController {

    private final MatePostService matePostService;

    private final MatePostRepository matePostRepository;

    @GetMapping("/mate")
    public List<MatePost> getAllMatePost() {
        return matePostRepository.findAllByOrderByCreatedAtDesc();
    }

    @GetMapping("/mate/{id}")
    public MatePost getMatePost(@PathVariable Long id) {
        return matePostRepository.findById(id).orElse(null);
    }

    @PostMapping("/mate")
    public MatePost createMatePost(@RequestBody MatePostRequest matePostRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        return matePostService.createMatePost(matePostRequest, userEmail);
    }

    @PatchMapping("/mate/{id}")
    public ResponseEntity<MatePost> updateMatePost(
        @PathVariable Long id, @RequestBody MatePostRequest matePostRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        MatePost matePost = matePostService.updateMatePost(id, matePostRequest, userEmail);

        return ResponseEntity.status(HttpStatus.OK).body(matePost);
    }

    @DeleteMapping("/mate/{id}")
    public ResponseEntity<MatePost> deleteMatePost(@PathVariable Long id) {
        matePostService.deleteMatePost(id);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
