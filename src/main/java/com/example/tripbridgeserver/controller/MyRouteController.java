package com.example.tripbridgeserver.controller;

import com.example.tripbridgeserver.dto.MyRouteListResponse;
import com.example.tripbridgeserver.dto.MyRouteResponse;
import com.example.tripbridgeserver.service.MyRouteService;
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

    @PostMapping
    public ResponseEntity<String> createMyRoute() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        Long routeId = myRouteService.createRoute(userEmail);

        return ResponseEntity.ok("현재 동선이 저장 되었습니다. routeId: " + routeId);
    }

    @GetMapping("/{routeId}")
    public ResponseEntity<MyRouteResponse> getRoute(@PathVariable Long routeId) {
        MyRouteResponse myRouteResponse = myRouteService.getRoute(routeId);
        return ResponseEntity.ok(myRouteResponse);
    }

    @GetMapping("/list")
    public ResponseEntity<List<MyRouteListResponse>> getMyRoute() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        List<MyRouteListResponse> routeList = myRouteService.getRouteList(userEmail);
        return ResponseEntity.ok(routeList);
    }

    @PatchMapping("/{routeId}")
    public ResponseEntity<MyRouteResponse> updateMyRoute(
            @PathVariable Long routeId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer rate,
            @RequestParam(required = false) String comment) {

        MyRouteResponse myRouteResponse = myRouteService.updateMyRoute(routeId, name, rate, comment);
        return ResponseEntity.ok(myRouteResponse);
    }

    @DeleteMapping("/{routeId}")
    public ResponseEntity<String> deleteMyRoute(@PathVariable Long routeId) {
        myRouteService.deleteMyRoute(routeId);
        return ResponseEntity.ok("routeId: " + routeId + "이(가) 성공적으로 삭제되었습니다.");
    }
}
