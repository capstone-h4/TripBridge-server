package com.example.tripbridgeserver.controller;

import com.example.tripbridgeserver.dto.TripCommentRequest;
import com.example.tripbridgeserver.entity.*;
import com.example.tripbridgeserver.service.TripCommentService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TripCommentController {

    private final TripCommentService tripCommentService;

    @GetMapping("/trip/{id}/comment")
    public List<TripComment> getTripComment (@PathVariable Long id) {
        return tripCommentService.getTripCommentByTripPost(id);
    }

    @PostMapping("/trip/comment")
    public ResponseEntity<TripComment> createTripComment(
        @RequestBody TripCommentRequest tripCommentRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        TripComment tripComment = tripCommentService.createTripComment(tripCommentRequest, userEmail);

        return ResponseEntity.status(HttpStatus.CREATED).body(tripComment);
    }

    @PatchMapping("/trip/comment/{id}")
    public ResponseEntity<TripComment> updateTripComment(
        @PathVariable Long id, @RequestBody TripCommentRequest tripCommentRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        TripComment tripComment = tripCommentService.updateTripComment(id, tripCommentRequest, userEmail);

        return ResponseEntity.status(HttpStatus.OK).body(tripComment);
    }

    @DeleteMapping("/trip/comment/{id}")
    public ResponseEntity<TripComment> deleteTripComment(@PathVariable Long id) {
        TripComment tripComment = tripCommentService.deleteTripComment(id);

        return ResponseEntity.status(HttpStatus.OK).body(tripComment);
    }
}
