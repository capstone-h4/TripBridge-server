package com.example.tripbridgeserver.service;

import com.example.tripbridgeserver.dto.MyPlaceResponse;
import com.example.tripbridgeserver.dto.MyRouteListResponse;
import com.example.tripbridgeserver.dto.MyRouteResponse;
import com.example.tripbridgeserver.entity.ChatRoute;
import com.example.tripbridgeserver.entity.MyPlace;
import com.example.tripbridgeserver.entity.MyRoute;
import com.example.tripbridgeserver.entity.User;
import com.example.tripbridgeserver.repository.ChatRouteRepository;
import com.example.tripbridgeserver.repository.MyPlaceRepository;
import com.example.tripbridgeserver.repository.MyRouteRepository;
import com.example.tripbridgeserver.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyRouteService {

    private final UserRepository userRepository;
    private final ChatRouteRepository chatRouteRepository;
    private final MyRouteRepository myRouteRepository;
    private final MyPlaceRepository myPlaceRepository;

    @Transactional
    public Long createRoute(String userEmail) {

        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }

        // 기존 MyRoute 목록에서 이름이 "동선"으로 시작하는 것들 중 가장 큰 숫자 찾기
        List<MyRoute> existingRoutes = myRouteRepository.findByUserAndNameStartingWith(user, "동선");
        int maxSuffix = 0;
        for (MyRoute route : existingRoutes) {
            String name = route.getName();
            if (name.length() > 2) {
                try {
                    int suffix = Integer.parseInt(name.substring(2));
                    if (suffix > maxSuffix) {
                        maxSuffix = suffix;
                    }
                } catch (NumberFormatException e) {

                }
            }
        }

        String newRouteName = "동선" + (maxSuffix + 1); // 새 동선 이름 설정

        // ChatRoute에서 place와 address 목록 가져오기
        List<ChatRoute> chatRoutes = chatRouteRepository.findByUserId(user.getId());

        MyRoute myRoute = new MyRoute();
        myRoute.setName(newRouteName);
        myRoute.setRate(0);
        myRoute.setComment(null);
        myRoute.setUser(user);
        myRouteRepository.save(myRoute);

        for (ChatRoute chatRoute : chatRoutes) {
            MyPlace myPlace = new MyPlace();
            myPlace.setPlace(chatRoute.getPlace());
            myPlace.setAddress(chatRoute.getAddress());
            myPlace.setRouteOrder(chatRoute.getRouteOrder());
            myPlace.setMyRoute(myRoute);
            myPlaceRepository.save(myPlace);
        }

        return myRoute.getId();
    }

    public MyRouteResponse getRoute(Long routeId) {

        MyRoute myRoute = myRouteRepository.findById(routeId)
                .orElseThrow(() -> new RuntimeException("동선을 찾을 수 없습니다."));
        
        List<MyPlaceResponse> myPlaces = myPlaceRepository.findByMyRoute(myRoute)
                .stream()
                .map(place -> new MyPlaceResponse(
                        place.getId(),
                        place.getPlace(),
                        place.getAddress(),
                        place.getRouteOrder()
                ))
                .toList();
        
        return new MyRouteResponse(
                myRoute.getId(),
                myRoute.getName(),
                myRoute.getRate(),
                myRoute.getComment(),
                myPlaces
        );
    }
    
    public List<MyRouteListResponse> getRouteList(String userEmail) {
        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }

        List<MyRoute> routes = myRouteRepository.findByUserId(user.getId());
        return routes.stream()
                .map(route -> new MyRouteListResponse(
                        route.getId(),
                        route.getName(),
                        route.getRate(),
                        route.getComment()
                ))
                .toList();
    }

    
    @Transactional
    public MyRouteResponse updateMyRoute(Long routeId, String name, Integer rate, String comment) {
        MyRoute myRoute = myRouteRepository.findById(routeId)
                .orElseThrow(() -> new RuntimeException("저장된 동선가 없습니다."));

        if (name != null) myRoute.setName(name);
        if (rate != null) {
            if (rate < 0 || rate > 5) {
                throw new IllegalArgumentException("평점은 0~5점만 가능합니다.");
            }
            myRoute.setRate(rate);
        }
        if (comment != null) myRoute.setComment(comment);

        myRouteRepository.save(myRoute);
        
        List<MyPlaceResponse> myPlaces = myPlaceRepository.findByMyRoute(myRoute)
                .stream()
                .map(place -> new MyPlaceResponse(
                        place.getId(),
                        place.getPlace(),
                        place.getAddress(),
                        place.getRouteOrder()
                ))
                .toList();

        return new MyRouteResponse(
                myRoute.getId(),
                myRoute.getName(),
                myRoute.getRate(),
                myRoute.getComment(),
                myPlaces
        );
    }
    
    @Transactional
    public void deleteMyRoute(Long routeId) {
        MyRoute myRoute = myRouteRepository.findById(routeId)
                .orElseThrow(() -> new RuntimeException("동선을 찾을 수 없습니다."));
        
        myPlaceRepository.deleteByMyRoute(myRoute); // 동선에 저장된 장소들도 삭제
        
        myRouteRepository.delete(myRoute);
    }
}
