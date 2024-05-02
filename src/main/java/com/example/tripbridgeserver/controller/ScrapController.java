package com.example.tripbridgeserver.controller;

import com.example.tripbridgeserver.common.ResponseDTO;
import com.example.tripbridgeserver.dto.ScrapDTO;
import com.example.tripbridgeserver.entity.Scrap;
import com.example.tripbridgeserver.service.ScrapService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Scrap create(@RequestBody ScrapDTO dto) {
        return scrapService.create(dto);
    }


    // 스크랩 삭제
    @DeleteMapping("/storage/{id}")
    public ResponseEntity<ResponseDTO<Void>> delete(@PathVariable Long id) {
        ResponseEntity<ResponseDTO<Void>> responseEntity = scrapService.delete(id);
        return ResponseEntity.status(responseEntity.getStatusCode()).body(responseEntity.getBody());
    }



    //    @DeleteMapping("/storage/{id}")
//    public ResponseEntity<Scrap> delete(@PathVariable Long id) {
//        return scrapService.delete(id);
//    }


}
