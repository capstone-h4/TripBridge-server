package com.example.tripbridgeserver.controller;

import com.example.tripbridgeserver.dto.MateCommentRequest;
import com.example.tripbridgeserver.entity.MateComment;
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

    @GetMapping("/mate/{id}/comment")
    public List<MateComment> getMateComment(@PathVariable Long id) {
        return mateCommentService.getMateCommentByMatePost(id);
    }

    @PostMapping("/mate/comment")
    public ResponseEntity<MateComment> createMateComment(
        @RequestBody MateCommentRequest mateCommentRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        MateComment mateComment = mateCommentService.createMateComment(mateCommentRequest, userEmail);

        return ResponseEntity.status(HttpStatus.CREATED).body(mateComment);
    }

    @PatchMapping("/mate/comment/{id}")
    public ResponseEntity<MateComment> updateMateComment(
        @PathVariable Long id, @RequestBody MateCommentRequest mateCommentRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        MateComment mateComment = mateCommentService.updateMateComment(id, mateCommentRequest, userEmail);

        if ( mateComment == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body( mateComment);
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
