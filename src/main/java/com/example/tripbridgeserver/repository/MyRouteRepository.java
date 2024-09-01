package com.example.tripbridgeserver.repository;

import com.example.tripbridgeserver.entity.MyRoute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyRouteRepository extends JpaRepository<MyRoute, Long> {
}
