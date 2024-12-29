package com.example.tripbridgeserver.controller;

import com.example.tripbridgeserver.dto.TripPostRequest;
import com.example.tripbridgeserver.entity.TripPost;
import com.example.tripbridgeserver.repository.TripPostRepository;
import com.example.tripbridgeserver.service.TripPostService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TripPostController {

    private final TripPostService tripPostService;
    private final TripPostRepository tripPostRepository;

    @GetMapping("/trip")
    public List<TripPost> getAllTripPost() {
        return tripPostRepository.findAllByOrderByCreatedAtDesc();
    }

    @GetMapping("/trip/{id}")
    public TripPost getTripPost(@PathVariable Long id) {
        return tripPostRepository.findById(id).orElse(null);
    }

    @PostMapping("/trip")
    public TripPost createTripPost(@ModelAttribute TripPostRequest tripPostRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        return tripPostService.createTripPost(tripPostRequest, userEmail);
    }

    @DeleteMapping("/trip/{id}")
    public ResponseEntity<TripPost> deleteTripPost(@PathVariable Long id) {
        tripPostService.deleteTripPost(id);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

