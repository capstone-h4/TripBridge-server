package com.example.tripbridgeserver.controller;

import com.example.tripbridgeserver.service.MyRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/myroute")
public class MyRouteController {


    @Autowired
    private MyRouteService myRouteService;

    // 동선 저장
    @PostMapping
    public ResponseEntity<String> createMyPlacesForCurrentUser() {
        try {
            // 사용자 인증 후 이메일 확인
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userEmail = authentication.getName();

            myRouteService.createMyRoutes(userEmail);

            return ResponseEntity.ok("현재 동선이 저장 되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("현재 동선 저장에 실패하였습니다. " + e.getMessage());
        }
    }






}
