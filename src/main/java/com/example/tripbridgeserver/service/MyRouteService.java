package com.example.tripbridgeserver.service;

import com.example.tripbridgeserver.dto.MyPlaceResponseDTO;
import com.example.tripbridgeserver.dto.MyRouteDTO;
import com.example.tripbridgeserver.dto.MyRouteResponseDTO;
import com.example.tripbridgeserver.entity.ChatRoute;
import com.example.tripbridgeserver.entity.MyPlace;
import com.example.tripbridgeserver.entity.MyRoute;
import com.example.tripbridgeserver.entity.UserEntity;
import com.example.tripbridgeserver.repository.ChatRouteRepository;
import com.example.tripbridgeserver.repository.MyPlaceRepository;
import com.example.tripbridgeserver.repository.MyRouteRepository;
import com.example.tripbridgeserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MyRouteService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatRouteRepository chatRouteRepository;

    @Autowired
    private MyRouteRepository myRouteRepository;

    @Autowired
    private MyPlaceRepository myPlaceRepository;

    // 루트 저장
    @Transactional
    public Long createMyRoutes(String userEmail) {

        // 사용자 확인
        UserEntity user = userRepository.findByEmail(userEmail);
        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }

        // 기존 MyRoute 목록에서 이름이 "동선"으로 시작하는 것들 중 가장 큰 숫자 찾기
        List<MyRoute> existingRoutes = myRouteRepository.findByUserEntityAndNameStartingWith(user, "동선");
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

        // 새 동선 이름 설정
        String newRouteName = "동선" + (maxSuffix + 1);

        // ChatRoute에서 userId로 place와 address 목록 가져오기
        List<ChatRoute> chatRoutes = chatRouteRepository.findByUserEntityId(user.getId());

        // MyRoute 생성
        MyRoute myRoute = new MyRoute();
        myRoute.setName(newRouteName);
        myRoute.setRate(0);
        myRoute.setComment(null);
        myRoute.setUserEntity(user);
        myRouteRepository.save(myRoute);


        // ChatRoute 데이터로 MyPlace 생성
        for (ChatRoute chatRoute : chatRoutes) {
            MyPlace myPlace = new MyPlace();
            myPlace.setPlace(chatRoute.getPlace());
            myPlace.setAddress(chatRoute.getAddress());
            myPlace.setMyRoute(myRoute);
            myPlaceRepository.save(myPlace);
        }

        return myRoute.getId();

    }


    // 루트 저장 관련 상세보기
    public MyRouteResponseDTO getMyRouteWithPlaces(Long routeId) {

        MyRoute myRoute = myRouteRepository.findById(routeId)
                .orElseThrow(() -> new RuntimeException("MyRoute not found"));

        // MyPlace 리스트 조회 및 DTO로 변환
        List<MyPlaceResponseDTO> myPlaces = myPlaceRepository.findByMyRoute(myRoute)
                .stream()
                .map(place -> new MyPlaceResponseDTO(
                        place.getId(),
                        place.getPlace(),
                        place.getAddress()
                ))
                .collect(Collectors.toList());

        // DTO로 반환
        return new MyRouteResponseDTO(
                myRoute.getId(),
                myRoute.getName(),
                myRoute.getRate(),
                myRoute.getComment(),
                myPlaces
        );
    }

    // 특정 사용자의 저장 루트 목록 조회
    public List<MyRouteDTO> getMyRoutes(String userEmail) {
        UserEntity user = userRepository.findByEmail(userEmail);
        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }

        List<MyRoute> routes = myRouteRepository.findByUserEntityId(user.getId());
        return routes.stream()
                .map(route -> new MyRouteDTO(
                        route.getId(),
                        route.getName(),
                        route.getRate(),
                        route.getComment()
                ))
                .collect(Collectors.toList());
    }


    // 저장 루트 수정
    @Transactional
    public MyRouteResponseDTO updateMyRoute(Long routeId, String name, Integer rate, String comment) {
        MyRoute myRoute = myRouteRepository.findById(routeId)
                .orElseThrow(() -> new RuntimeException("저장된 루트가 없습니다."));

        if (name != null) myRoute.setName(name);
        if (rate != null) {
            if (rate < 0 || rate > 5) {
                throw new IllegalArgumentException("평점은 0~5점만 가능합니다.");
            }
            myRoute.setRate(rate);
        }
        if (comment != null) myRoute.setComment(comment);

        myRouteRepository.save(myRoute);

        // DTO로 변환
        List<MyPlaceResponseDTO> myPlaces = myPlaceRepository.findByMyRoute(myRoute)
                .stream()
                .map(place -> new MyPlaceResponseDTO(
                        place.getId(),
                        place.getPlace(),
                        place.getAddress()
                ))
                .collect(Collectors.toList());

        return new MyRouteResponseDTO(
                myRoute.getId(),
                myRoute.getName(),
                myRoute.getRate(),
                myRoute.getComment(),
                myPlaces
        );
    }


    // 저장 동선 삭제 => 저장된 장소들도 함께 삭제
    @Transactional
    public void deleteMyRoute(Long routeId) {
        MyRoute myRoute = myRouteRepository.findById(routeId)
                .orElseThrow(() -> new RuntimeException("동선을 찾을 수 없습니다."));

        // MyRoute에 연결된 장소들도 삭제
        myPlaceRepository.deleteByMyRoute(myRoute);

        // 동선 삭제
        myRouteRepository.delete(myRoute);
    }


}
