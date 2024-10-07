package com.example.tripbridgeserver.controller;


import com.example.tripbridgeserver.entity.Scrap;
import com.example.tripbridgeserver.entity.User;
import com.example.tripbridgeserver.repository.ScrapRepository;
import com.example.tripbridgeserver.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MapController {

    private final ScrapRepository scrapRepository;
    private final UserRepository userRepository;

    //동선 추천 페이지에서 유저의 스크랩목록 조회
    @GetMapping("/scrap")
    public List<Scrap> show(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User currentUser = userRepository.findByEmail(userEmail);
        List<Scrap> scraps = scrapRepository.findByUser(currentUser);
        return scraps;

    }
}
