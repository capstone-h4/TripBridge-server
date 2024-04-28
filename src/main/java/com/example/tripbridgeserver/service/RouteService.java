package com.example.tripbridgeserver.service;

import com.example.tripbridgeserver.dto.RouteDTO;
import com.example.tripbridgeserver.entity.Route;
import com.example.tripbridgeserver.repository.RouteRepository;
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
public class RouteService {
    private final RouteRepository routeRepository;


    @Autowired
    public RouteService(RouteRepository routeRepository){
        this.routeRepository = routeRepository;
    }

    public Route toEntity(RouteDTO dto){
        Route route = new Route();
        route.setPlace(dto.getPlace());
        route.setAddress(dto.getAddress());
        route.setLatitude(dto.getLatitude());
        route.setLongitude(dto.getLongitude());
        route.setRoute_order(dto.getRoute_order());

        return route;
    }

    public void calculateRouteOrder() {
        List<Route> routes = routeRepository.findAll();

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
                }
            }
        }

        // 경로 순서를 저장하는 맵
        // 첫 번째 노드를 출발점으로 선택하여 경로 순서 업데이트
        Route firstRoute = routes.get(0);
        String sourcePlace = firstRoute.getPlace(); // 첫 번째 노드를 출발점으로 설정
        Map<String, Integer> distanceMap = new HashMap<>();

        // 출발지를 설정하여 그래프를 생성
        // 출발지를 설정하여 그래프를 생성
        DijkstraShortestPath<String, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

        // 출발지를 설정하여 getPath 메서드를 호출
        for (Route targetRoute : routes) {
            if (!sourcePlace.equals(targetRoute.getPlace())) {
                GraphPath<String, DefaultWeightedEdge> shortestPath = dijkstraShortestPath.getPath(sourcePlace, targetRoute.getPlace());
                if (shortestPath != null) {
                    double distance = 0;
                    for (DefaultWeightedEdge edge : shortestPath.getEdgeList()) {
                        distance += graph.getEdgeWeight(edge);
                    }
                    distanceMap.put(targetRoute.getPlace(), (int) distance); // 거리 저장
                }
            }
        }

        // 거리에 따라 장소 정렬
        List<String> sortedPlaces = distanceMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // 데이터베이스에서 이미 할당된 route_order 값을 가져오기
        Map<String, Long> existingRouteOrders = routes.stream()
                .collect(Collectors.toMap(Route::getPlace, Route::getRoute_order));

        // 정렬된 장소를 기반으로 경로 순서 업데이트
        int routeOrder = 1;
        for (String place : sortedPlaces) {
            // 이미 route_order이 설정된 경우에는 기존 값 유지
            Long existingOrder = existingRouteOrders.get(place);
            if (existingOrder != null) {
                routeOrder = existingOrder.intValue();
            } else {
                // route_order이 설정되지 않은 경우에만 업데이트
                routeOrder = existingOrder != null ? existingOrder.intValue() : routeOrder;
                firstRoute.setRoute_order((long) routeOrder++);
            }
        }

        routeRepository.save(firstRoute);
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
