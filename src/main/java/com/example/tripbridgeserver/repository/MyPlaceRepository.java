package com.example.tripbridgeserver.repository;

import com.example.tripbridgeserver.entity.MyPlace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyPlaceRepository extends JpaRepository<MyPlace, Long> {
}
