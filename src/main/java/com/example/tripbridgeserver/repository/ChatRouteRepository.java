package com.example.tripbridgeserver.repository;

import com.example.tripbridgeserver.entity.ChatRoute;
import com.example.tripbridgeserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRouteRepository extends JpaRepository<ChatRoute, Long> {
    List<ChatRoute> findByUser(User currentUser);

    @Query("SELECT cr FROM ChatRoute cr WHERE cr.user = :user ORDER BY cr.routeOrder")
    List<ChatRoute> findByUserOrderByRouteOrder(@Param("user") User user);

    List<ChatRoute> findByUserId(Long id);
}
