package com.example.tripbridgeserver.controller;

import com.example.tripbridgeserver.dto.MyRouteDTO;
import com.example.tripbridgeserver.dto.MyRouteResponseDTO;
import com.example.tripbridgeserver.service.MyRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/myroute")
public class MyRouteController {

    private final MyRouteService myRouteService;

    // 루트 저장
    @PostMapping
    public ResponseEntity<String> createMyPlacesForCurrentUser() {
        try {
            // 사용자 인증 후 이메일 확인
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userEmail = authentication.getName();

            Long routeId = myRouteService.createMyRoutes(userEmail);

            return ResponseEntity.ok("현재 동선이 저장 되었습니다. (routeId: " + routeId + ")");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("현재 동선 저장에 실패하였습니다. " + e.getMessage());
        }
    }


    // 루트 저장 조회
    @GetMapping("/{routeId}")
    public ResponseEntity<MyRouteResponseDTO> getMyRouteWithPlaces(@PathVariable Long routeId) {
        try {
            MyRouteResponseDTO myRouteResponse = myRouteService.getMyRouteWithPlaces(routeId);
            return ResponseEntity.ok(myRouteResponse);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    // 특정 사용자의 저장 루트 목록 조회
    @GetMapping("/list")
    public ResponseEntity<List<MyRouteDTO>> getMyRouteList() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userEmail = authentication.getName();

            List<MyRouteDTO> routeList = myRouteService.getMyRoutes(userEmail);
            return ResponseEntity.ok(routeList);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }


    // 저장 루트 수정
    @PatchMapping("/{routeId}")
    public ResponseEntity<MyRouteResponseDTO> updateMyRoute(
            @PathVariable Long routeId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer rate,
            @RequestParam(required = false) String comment) {

        try {
            MyRouteResponseDTO updatedRoute = myRouteService.updateMyRoute(routeId, name, rate, comment);
            return ResponseEntity.ok(updatedRoute);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) { // 동선이 없는 경우
            return ResponseEntity.status(404).body(null);
        }
    }


    // 저장 동선 삭제 => 저장된 장소들도 함께 삭제
    @DeleteMapping("/{routeId}")
    public ResponseEntity<String> deleteMyRoute(@PathVariable Long routeId) {
        try {
            myRouteService.deleteMyRoute(routeId);
            return ResponseEntity.ok("routeId: " + routeId + "이(가) 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(404).body("동선을 찾을 수 없습니다.");
        }
    }


}
