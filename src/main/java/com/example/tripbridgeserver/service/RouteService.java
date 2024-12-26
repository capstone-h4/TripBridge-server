package com.example.tripbridgeserver.service;

import com.example.tripbridgeserver.dto.RouteRequest;
import com.example.tripbridgeserver.entity.ChatRoute;
import com.example.tripbridgeserver.entity.Route;
import com.example.tripbridgeserver.entity.User;
import com.example.tripbridgeserver.repository.ChatRouteRepository;
import com.example.tripbridgeserver.repository.RouteRepository;
import com.example.tripbridgeserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final RouteRepository routeRepository;
    private final ChatRouteRepository chatRouteRepository;
    private final UserRepository userRepository;

    public void deleteUserChatRoute(String userEmail) {
        User user = userRepository.findByEmail(userEmail);
        List<ChatRoute> chatRoutes = chatRouteRepository.findByUser(user);
        chatRouteRepository.deleteAll(chatRoutes);
    }

    public Route enrollUserRoute(RouteRequest routeRequest, String userEmail) {
        User user = userRepository.findByEmail(userEmail);
        Route route = toEntity(routeRequest,user);

        return routeRepository.save(route);
    }

    public Route toEntity(RouteRequest routeRequest, User user){
        Route route = new Route();
        route.setPlace(routeRequest.getPlace());
        route.setAddress(routeRequest.getAddress());
        route.setLatitude(routeRequest.getLatitude());
        route.setLongitude(routeRequest.getLongitude());
        route.setRoute_order(routeRequest.getRoute_order());
        route.setUser(user);

        return route;
    }

    public void modifyRouteOrder() {
        List<Route> routes = routeRepository.findAll();

        // 초기 노드를 찾기 위해 데이터를 순차적으로 확인하면서 route_order가 1인 노드를 찾기
        Route initialRoute = null;
        for (Route route : routes) {
            Long routeOrder = route.getRoute_order();
            if (routeOrder != null && routeOrder == 1) {
                initialRoute = route;
                break;
            }
        }

        // 초기 노드가 없는 경우 처리
        if (initialRoute == null) {
            // 처리할 초기 노드가 없으므로 여기서 종료하거나 예외 처리를 수행
            return;
        }

        // 초기 노드를 기준으로 TSP 알고리즘을 사용하여 경로 계산
        List<Route> optimizedRoute = tsp(initialRoute, routes);

        // 경로 순서를 업데이트
        for (int i = 0; i < optimizedRoute.size(); i++) {
            Route route = optimizedRoute.get(i);
            route.setRoute_order((long) (i + 1)); // 경로 순서는 1부터 시작
            routeRepository.save(route);
        }
    }

    // TSP 알고리즘 구현
    private List<Route> tsp(Route initialRoute, List<Route> routes) {
        List<Route> optimizedRoute = new ArrayList<>();
        List<Route> remainingRoutes = new ArrayList<>(routes);
        remainingRoutes.remove(initialRoute);
        optimizedRoute.add(initialRoute);

        while (!remainingRoutes.isEmpty()) {
            Route lastRoute = optimizedRoute.get(optimizedRoute.size() - 1);
            Route nearestRoute = null;
            double shortestDistance = Double.MAX_VALUE;

            for (Route route : remainingRoutes) {
                double distance = calculateDistance(lastRoute.getLatitude(), lastRoute.getLongitude(),
                        route.getLatitude(), route.getLongitude());
                if (distance < shortestDistance) {
                    shortestDistance = distance;
                    nearestRoute = route;
                }
            }

            if (nearestRoute != null) {
                optimizedRoute.add(nearestRoute);
                remainingRoutes.remove(nearestRoute);
            }
        }

        return optimizedRoute;
    }

    private double calculateDistance(BigDecimal lat1, BigDecimal lon1, BigDecimal lat2, BigDecimal lon2) {
        // 위도와 경도의 차이를 제곱하여 더하고, 제곱근을 취해서 거리를 구하기
        double latDiff = lat2.doubleValue() - lat1.doubleValue();
        double lonDiff = lon2.doubleValue() - lon1.doubleValue();
        return Math.sqrt(latDiff * latDiff + lonDiff * lonDiff);
    }

    public void copyRoutesToChatRoute() {
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
                chatRoute.setUser(route.getUser());

                chatRouteRepository.save(chatRoute);

                processedRouteIds.put(route.getId(), true); // 처리된 Route 의 ID를 Map 에 추가
            }
        }
    }
}
