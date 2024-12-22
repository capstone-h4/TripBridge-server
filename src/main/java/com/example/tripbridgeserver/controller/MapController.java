package com.example.tripbridgeserver.controller;

import com.example.tripbridgeserver.entity.Scrap;
import com.example.tripbridgeserver.service.MapService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MapController {

    private final MapService mapService;

    // 동선 추천 페이지에서 사용자의 스크랩 목록 조회
    @GetMapping("/scrap")
    public List<Scrap> getScrap(){
        return mapService.getUserScrap();
    }
}
