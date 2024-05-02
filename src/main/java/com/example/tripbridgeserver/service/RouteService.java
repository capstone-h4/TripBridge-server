package com.example.tripbridgeserver.service;

import com.example.tripbridgeserver.dto.RouteDTO;
import com.example.tripbridgeserver.entity.Route;
import com.example.tripbridgeserver.entity.UserEntity;
import com.example.tripbridgeserver.repository.RouteRepository;
import lombok.extern.slf4j.Slf4j;
import org.jgrapht.GraphPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
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

        String initialPlace = initialRoute.getPlace();

        // 장소 간 연결을 나타내는 그래프 생성
        Graph<String, DefaultWeightedEdge> graph = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);

        // 그래프에 정점(장소) 추가
        for (Route route : routes) {
            graph.addVertex(route.getPlace());
        }

        // 그래프에 간선(장소 간 연결) 추가
        for (Route route1 : routes) {
            for (Route route2 : routes) {
                if (!route1.equals(route2)) {
                    double distance = calculateDistance(route1.getLatitude(), route1.getLongitude(), route2.getLatitude(), route2.getLongitude());
                    DefaultWeightedEdge edge = graph.addEdge(route1.getPlace(), route2.getPlace());
                    graph.setEdgeWeight(edge, distance);
                    log.info("Edge from {} to {} added with weight: {}", route1.getPlace(), route2.getPlace(), distance);
                }
            }
        }

        // 경로 순서를 저장하는 맵
        Map<String, Integer> distanceMap = new HashMap<>();

        // 출발지를 설정하여 그래프를 생성
        DijkstraShortestPath<String, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

        // 출발지에서 모든 다른 장소로의 최단 거리를 계산하여 맵에 저장
        Map<String, Double> distancesFromInitial = new HashMap<>();
        for (Route targetRoute : routes) {
            if (!initialPlace.equals(targetRoute.getPlace())) {
                GraphPath<String, DefaultWeightedEdge> shortestPath = dijkstraShortestPath.getPath(initialPlace, targetRoute.getPlace());
                if (shortestPath != null) {
                    double distance = 0;
                    for (DefaultWeightedEdge edge : shortestPath.getEdgeList()) {
                        distance += graph.getEdgeWeight(edge);
                    }
                    distancesFromInitial.put(targetRoute.getPlace(), distance);
                }
            }
        }

        // 거리에 따라 장소 정렬하여 거리 맵에 저장
        List<String> sortedPlaces = distancesFromInitial.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .toList();

        // 경로 순서 맵을 업데이트
        int routeOrder = 1;
        for (String place : sortedPlaces) {
            distanceMap.put(place, ++routeOrder);
        }

        // 경로 순서 맵을 이용하여 모든 루트의 route_order 업데이트
        for (Route route : routes) {
            Integer routeOrderValue = distanceMap.get(route.getPlace());
            if (routeOrderValue != null) {
                route.setRoute_order((long) routeOrderValue);
                routeRepository.save(route);
            }
        }
    }


    private double calculateDistance(BigDecimal lat1, BigDecimal lon1, BigDecimal lat2, BigDecimal lon2) {
        // 위도와 경도의 차이를 제곱하여 더하고, 제곱근을 취해서 거리를 구합니다.
        double latDiff = lat2.doubleValue() - lat1.doubleValue();
        double lonDiff = lon2.doubleValue() - lon1.doubleValue();
        double distance = Math.sqrt(latDiff * latDiff + lonDiff * lonDiff);

        // 결과를 미터 단위로 반환합니다.
        return distance * 111000; // 대략적인 값으로 위도 1도당 약 111 킬로미터라고 가정합니다.
    }

}
