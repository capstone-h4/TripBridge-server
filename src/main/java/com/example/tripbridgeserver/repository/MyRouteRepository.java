package com.example.tripbridgeserver.repository;

import com.example.tripbridgeserver.entity.MyRoute;
import com.example.tripbridgeserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MyRouteRepository extends JpaRepository<MyRoute, Long> {
    List<MyRoute> findByUserId(Long id);
    List<MyRoute> findByUserAndNameStartingWith(User user, String namePrefix);
}
