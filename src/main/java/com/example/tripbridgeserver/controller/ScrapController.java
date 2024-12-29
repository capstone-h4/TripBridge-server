package com.example.tripbridgeserver.controller;

import com.example.tripbridgeserver.dto.ResponseDTO;
import com.example.tripbridgeserver.dto.ScrapRequest;
import com.example.tripbridgeserver.entity.Scrap;
import com.example.tripbridgeserver.service.ScrapService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ScrapController {

    private final ScrapService scrapService;

    @PostMapping("/storage") // 장소 스크랩 생성
    public ResponseEntity<ResponseDTO<Scrap>> createScrap(@RequestBody ScrapRequest scrapRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        ResponseDTO<Scrap> responseDTO = scrapService.createPlaceScrap(scrapRequest, userEmail);

        if (responseDTO.isResult()) {
            return ResponseEntity.ok(responseDTO);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }

    @DeleteMapping("/storage/{id}") // 장소 스크랩 삭제
    public ResponseEntity<ResponseDTO<Void>> deleteScrap(@PathVariable Long id) {
        ResponseEntity<ResponseDTO<Void>> responseEntity = scrapService.deletePlaceScrap(id);

        return ResponseEntity.status(responseEntity.getStatusCode()).body(responseEntity.getBody());
    }
}
