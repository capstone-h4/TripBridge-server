package com.example.tripbridgeserver.repository;

import com.example.tripbridgeserver.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route,Long> {
    List<Route> findAllByOrderByRouteOrderAsc();
}
