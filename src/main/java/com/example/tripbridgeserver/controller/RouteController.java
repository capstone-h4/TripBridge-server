package com.example.tripbridgeserver.controller;

import com.example.tripbridgeserver.dto.RouteDTO;
import com.example.tripbridgeserver.entity.ChatRoute;
import com.example.tripbridgeserver.entity.MatePost;
import com.example.tripbridgeserver.entity.Route;
import com.example.tripbridgeserver.entity.UserEntity;
import com.example.tripbridgeserver.repository.ChatRouteRepository;
import com.example.tripbridgeserver.repository.RouteRepository;
import com.example.tripbridgeserver.repository.UserRepository;
import com.example.tripbridgeserver.service.RouteService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RouteController {
    private final RouteService routeService;
    private final RouteRepository routeRepository;
    private final UserRepository userRepository;
    private final ChatRouteRepository chatRouteRepository;

    @Autowired
    public RouteController(RouteService routeService, RouteRepository routeRepository, UserRepository userRepository, ChatRouteRepository chatRouteRepository){
        this.routeService = routeService;
        this.routeRepository = routeRepository;
        this.userRepository = userRepository;
        this.chatRouteRepository = chatRouteRepository;
    }
    @DeleteMapping("route/chat")
    public void deleteAllChatRoute(){
        chatRouteRepository.deleteAll();
    }

    @PostMapping("/route")
    public Route enroll(@RequestBody RouteDTO dto){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        UserEntity currentUser = userRepository.findByEmail(userEmail);

        Route route = routeService.toEntity(dto,currentUser);
        return routeRepository.save(route);
    }

    @PostMapping("/route/update")
    public void updateRoutes() {
        routeService.calculateRouteOrder();
    }
    @GetMapping("/route")
    public List<Route> index(){
        return routeRepository.findAll();
    }

    @PostMapping("route/chat")
    public void copyToChatRoute(){
        List<Route> routes = routeRepository.findAll();
        for (Route route : routes) {
            ChatRoute chatRoute = new ChatRoute();

            chatRoute.setPlace(route.getPlace());
            chatRoute.setAddress(route.getAddress());
            chatRoute.setRoute_order(route.getRoute_order());
            chatRoute.setLatitude(route.getLatitude());
            chatRoute.setLongitude(route.getLongitude());
            chatRoute.setUserEntity(route.getUserEntity());

            chatRouteRepository.save(chatRoute);
        }
    }

    @DeleteMapping("/route")
    public void deleteAllRoutes(){
        routeRepository.deleteAll();
    }

}
