package com.example.tripbridgeserver.controller;

import com.example.tripbridgeserver.service.ChatBotService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ChatBotController {

    private final ChatBotService chatBotService;

    // 주변 관광지 추천
    @PostMapping("/chatBot/question1")
    public String getNearPlace(@RequestBody String choicePlace){
        return chatBotService.generateNearPlaces(choicePlace);
    }

    // 관광지 상세정보
    @PostMapping("/chatBot/question2")
    public String getPlaceDetail(@RequestBody String choicePlace){
        return chatBotService.generatePlaceDetail(choicePlace);
    }

    // 동선 간의 이동수단, 예상비용 정보
    @GetMapping("/chatBot/question3")
    public String getTransferAndCost() {
        return chatBotService.generateTransferAndCost();
    }

    // 여행 동선에 따른 일정 추천 정보
    @PostMapping("/chatBot/question4")
    public String getSchedule(@RequestBody String schedule) {
        return chatBotService.generateSchedule(schedule);
    }
}
