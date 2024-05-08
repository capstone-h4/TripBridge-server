package com.example.tripbridgeserver.repository;

import com.example.tripbridgeserver.entity.ChatRoute;
import com.example.tripbridgeserver.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRouteRepository extends JpaRepository<ChatRoute, Long> {

    List<ChatRoute> findByUserEntity(UserEntity currentUser);
}
