package com.example.tripbridgeserver.service;

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

}
