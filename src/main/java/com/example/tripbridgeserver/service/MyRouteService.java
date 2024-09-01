package com.example.tripbridgeserver.service;

import com.example.tripbridgeserver.dto.MyPlaceResponseDTO;
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

    // 동선 저장
    @Transactional
    public void createMyRoutes(String userEmail) {

        // 사용자 확인
        UserEntity user = userRepository.findByEmail(userEmail);
        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }

        // ChatRoute에서 userId로 place와 address 목록 가져오기
        List<ChatRoute> chatRoutes = chatRouteRepository.findByUserEntityId(user.getId());

        // MyRoute 생성
        MyRoute myRoute = new MyRoute();
        myRoute.setName("동선");
        myRoute.setRate(0);
        myRoute.setComment("코멘트");
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

    }


    // 동선 저장 관련 상세보기
    public MyRouteResponseDTO getMyRouteWithPlaces(Long routeId) {
        // MyRoute 조회
        MyRoute myRoute = myRouteRepository.findById(routeId)
                .orElseThrow(() -> new RuntimeException("MyRoute not found"));

        // MyRoute에 연결된 MyPlace 리스트 조회 및 DTO로 변환
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

}
