package com.example.tripbridgeserver.repository;

import com.example.tripbridgeserver.entity.MyPlace;
import com.example.tripbridgeserver.entity.MyRoute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MyPlaceRepository extends JpaRepository<MyPlace, Long> {
    List<MyPlace> findByMyRoute(MyRoute myRoute);

    void deleteByMyRoute(MyRoute myRoute);
}
