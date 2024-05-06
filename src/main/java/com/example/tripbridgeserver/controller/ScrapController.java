package com.example.tripbridgeserver.controller;

import com.example.tripbridgeserver.common.ResponseDTO;
import com.example.tripbridgeserver.dto.ScrapDTO;
import com.example.tripbridgeserver.entity.Scrap;
import com.example.tripbridgeserver.service.ScrapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ScrapController {
    private final ScrapService scrapService;

    @Autowired
    public ScrapController(ScrapService scrapService){
        this.scrapService = scrapService;
    }

    // 스크랩 생성

    @PostMapping("/storage")
    public ResponseEntity<ResponseDTO<Scrap>> create(@RequestBody ScrapDTO dto) {
        ResponseDTO<Scrap> responseDTO = scrapService.create(dto);

        if (responseDTO.isResult()) {
            return ResponseEntity.ok(responseDTO);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }

    // 스크랩 삭제
    @DeleteMapping("/storage/{id}")
    public ResponseEntity<ResponseDTO<Void>> delete(@PathVariable Long id) {
        ResponseEntity<ResponseDTO<Void>> responseEntity = scrapService.delete(id);
        return ResponseEntity.status(responseEntity.getStatusCode()).body(responseEntity.getBody());
    }

}
