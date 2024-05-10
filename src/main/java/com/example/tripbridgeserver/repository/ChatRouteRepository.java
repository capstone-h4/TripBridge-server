package com.example.tripbridgeserver.repository;

import com.example.tripbridgeserver.entity.ChatRoute;
import com.example.tripbridgeserver.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRouteRepository extends JpaRepository<ChatRoute, Long> {

    List<ChatRoute> findByUserEntity(UserEntity currentUser);

    @Query("SELECT cr FROM ChatRoute cr WHERE cr.userEntity = :userEntity ORDER BY cr.route_order")
    List<ChatRoute> findByUserEntityOrderByRouteOrder(@Param("userEntity") UserEntity userEntity);

}
