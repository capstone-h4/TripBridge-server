package com.example.tripbridgeserver.controller;

import com.example.tripbridgeserver.dto.MateCommentRequest;
import com.example.tripbridgeserver.entity.MateComment;
import com.example.tripbridgeserver.entity.User;
import com.example.tripbridgeserver.repository.UserRepository;
import com.example.tripbridgeserver.service.MateCommentService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MateCommentController {

    private final MateCommentService mateCommentService;

    private final UserRepository userRepository;

    @GetMapping("/mate/{id}/comment")
    public List<MateComment> getMateComment(@PathVariable Long id) {
        return mateCommentService.getMateCommentByMatePost(id);
    }

    @PostMapping("/mate/comment")
    public ResponseEntity<MateComment> createMateComment(
        @RequestBody MateCommentRequest mateCommentRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User user = userRepository.findByEmail(userEmail);

        MateComment savedComment = mateCommentService.createMateComment(mateCommentRequest, user);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedComment);
    }

    @PatchMapping("/mate/comment/{id}")
    public ResponseEntity<MateComment> updateMateComment(
        @PathVariable Long id, @RequestBody MateCommentRequest mateCommentRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User user = userRepository.findByEmail(userEmail);

        MateComment updatedComment = mateCommentService.updateMateComment(id, mateCommentRequest, user);

        if (updatedComment == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(updatedComment);
    }

    @DeleteMapping("/mate/comment/{id}")
    public ResponseEntity<MateComment> deleteMateComment(@PathVariable Long id) {
        boolean isDeleted = mateCommentService.deleteMateComment(id);

        if (!isDeleted) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
