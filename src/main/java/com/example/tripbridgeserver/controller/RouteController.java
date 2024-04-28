package com.example.tripbridgeserver.controller;

import com.example.tripbridgeserver.dto.RouteDTO;
import com.example.tripbridgeserver.entity.Route;
import com.example.tripbridgeserver.repository.RouteRepository;
import com.example.tripbridgeserver.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RouteController {
    private final RouteService routeService;
    private final RouteRepository routeRepository;

    @Autowired
    public RouteController(RouteService routeService, RouteRepository routeRepository){
        this.routeService = routeService;
        this.routeRepository = routeRepository;
    }

    @PostMapping("/route")
    public Route enroll(@RequestBody RouteDTO dto){

        Route route = routeService.toEntity(dto);
        return routeRepository.save(route);
    }


    @PostMapping("/route/update")
    public void updateRoutes() {
        routeService.calculateRouteOrder();
    }

}
