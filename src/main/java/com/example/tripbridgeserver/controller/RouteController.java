package com.example.tripbridgeserver.controller;


import com.example.tripbridgeserver.dto.RouteDTO;
import com.example.tripbridgeserver.entity.ChatRoute;
import com.example.tripbridgeserver.entity.Route;
import com.example.tripbridgeserver.entity.UserEntity;
import com.example.tripbridgeserver.repository.ChatRouteRepository;
import com.example.tripbridgeserver.repository.RouteRepository;
import com.example.tripbridgeserver.repository.UserRepository;
import com.example.tripbridgeserver.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

//
@RestController
public class RouteController {

    private final RouteService routeService;
    private final RouteRepository routeRepository;
    private final UserRepository userRepository;
    private final ChatRouteRepository chatRouteRepository;

    @Autowired
    public RouteController(RouteService routeService, RouteRepository routeRepository, UserRepository userRepository, ChatRouteRepository chatRouteRepository, RestTemplate restTemplate){
        this.routeService = routeService;
        this.routeRepository = routeRepository;
        this.userRepository = userRepository;
        this.chatRouteRepository = chatRouteRepository;
    }
    //현재 User 의 새로운 route 를 생성하기 전에 이전 route 삭제
    @DeleteMapping("route/chat")
    public void deleteUsersChatRoute(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        UserEntity currentUser = userRepository.findByEmail(userEmail);

        List<ChatRoute> chatRoutes = chatRouteRepository.findByUserEntity(currentUser);
        chatRouteRepository.deleteAll(chatRoutes);
    }
    //새로운 route 생성
    @PostMapping("/route")
    public Route enroll(@RequestBody RouteDTO dto){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        UserEntity currentUser = userRepository.findByEmail(userEmail);

        Route route = routeService.toEntity(dto,currentUser);
        return routeRepository.save(route);
    }
    //route 방문 순서 갱신
    @PostMapping("/route/update")
    public void updateRoutes() {
        routeService.calculateRouteOrder();
    }
    //route 순서 갱신정보 포함 route 정보 조회
    @GetMapping("/route")
    public List<Route> index(){
        return routeRepository.findAll();
    }
    //route 정보를 chatBot 에 사용하기 위해 chatRoute 데이터 베이스로 복사
    @PostMapping("route/chat")
    @Transactional
    public void copyToChatRoute(){
        Map<Long, Boolean> processedRouteIds = new HashMap<>(); // 이미 처리된 Route 의 ID를 저장하는 Map

        List<Route> routes = routeRepository.findAll();
        for (Route route : routes) {
            if (!processedRouteIds.containsKey(route.getId())) { // 이미 처리된 Route 인지 확인
                ChatRoute chatRoute = new ChatRoute();

                chatRoute.setPlace(route.getPlace());
                chatRoute.setAddress(route.getAddress());
                chatRoute.setRoute_order(route.getRoute_order());
                chatRoute.setLatitude(route.getLatitude());
                chatRoute.setLongitude(route.getLongitude());
                chatRoute.setUserEntity(route.getUserEntity());

                chatRouteRepository.save(chatRoute);

                processedRouteIds.put(route.getId(), true); // 처리된 Route 의 ID를 Map 에 추가
            }
        }
    }
    //route 들을 방문 순서를 갱신하고 User 들 간의 데이터 중복을 막기위해 즉시 삭제
    @DeleteMapping("/route")
    public void deleteAllRoutes(){
        routeRepository.deleteAll();
    }
}
