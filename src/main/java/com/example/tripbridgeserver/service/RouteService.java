package com.example.tripbridgeserver.service;

import com.example.tripbridgeserver.dto.RouteDTO;
import com.example.tripbridgeserver.entity.Route;
import com.example.tripbridgeserver.entity.UserEntity;
import com.example.tripbridgeserver.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;



@Service
public class RouteService {
    private final RouteRepository routeRepository;


    @Autowired
    public RouteService(RouteRepository routeRepository){
        this.routeRepository = routeRepository;
    }

    public Route toEntity(RouteDTO dto, UserEntity currentUser){
        Route route = new Route();
        route.setPlace(dto.getPlace());
        route.setAddress(dto.getAddress());
        route.setLatitude(dto.getLatitude());
        route.setLongitude(dto.getLongitude());
        route.setRoute_order(dto.getRoute_order());
        route.setUserEntity(currentUser);

        return route;
    }

    public void calculateRouteOrder() {
        List<Route> routes = routeRepository.findAll();

        // 초기 노드를 찾기 위해 데이터를 순차적으로 확인하면서 route_order가 1인 노드를 찾습니다.
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
            // 처리할 초기 노드가 없으므로 여기서 종료하거나 예외 처리를 수행할 수 있습니다.
            return;
        }

        // 초기 노드를 기준으로 TSP 알고리즘을 사용하여 경로 계산
        List<Route> optimizedRoute = tsp(initialRoute, routes);

        // 경로 순서를 업데이트
        for (int i = 0; i < optimizedRoute.size(); i++) {
            Route route = optimizedRoute.get(i);
            route.setRoute_order((long) (i + 1)); // 경로 순서는 1부터 시작합니다.
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
        // 위도와 경도의 차이를 제곱하여 더하고, 제곱근을 취해서 거리를 구합니다.
        double latDiff = lat2.doubleValue() - lat1.doubleValue();
        double lonDiff = lon2.doubleValue() - lon1.doubleValue();
        return Math.sqrt(latDiff * latDiff + lonDiff * lonDiff);
    }
}