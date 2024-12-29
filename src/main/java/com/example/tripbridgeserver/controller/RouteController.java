package com.example.tripbridgeserver.controller;

import com.example.tripbridgeserver.dto.RouteRequest;
import com.example.tripbridgeserver.entity.Route;
import com.example.tripbridgeserver.repository.RouteRepository;
import com.example.tripbridgeserver.service.RouteService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RouteController {

    private final RouteService routeService;
    private final RouteRepository routeRepository;

    @DeleteMapping("route/chat") // user의 새로운 route를 생성하기 전, 이전에 저장되어 있던 route 삭제
    public void deleteChatRoute() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        routeService.deleteUserChatRoute(userEmail);
    }

    @PostMapping("/route") // 새로운 route 생성
    public Route enrollRoute(@RequestBody RouteRequest routeRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        return routeService.enrollUserRoute(routeRequest, userEmail);
    }

    @PostMapping("/route/update") // route 방문 순서 갱신
    public void updateRouteOrder() {
        routeService.modifyRouteOrder();
    }

    @GetMapping("/route") // 갱신된 정보를 포함한 모든 route 순서 조회
    public List<Route> getRouteOrder() {
        return routeRepository.findAll();
    }

    @PostMapping("route/chat") // route 정보를 챗봇에서 사용하기 위해 chatRoute 테이블로 복사
    @Transactional
    public void copyRoutesToChatRoute() {
        routeService.copyRoutesToChatRoute();
    }

    @DeleteMapping("/route") //route 들을 방문 순서를 갱신하고 User 간의 데이터 중복을 막기위해 즉시 삭제
    public void deleteAllRoutes(){
        routeRepository.deleteAll();
    }
}
