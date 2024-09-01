package com.example.tripbridgeserver.repository;

import com.example.tripbridgeserver.entity.MyRoute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MyRouteRepository extends JpaRepository<MyRoute, Long> {
    List<MyRoute> findByUserEntityId(Long id);
}
