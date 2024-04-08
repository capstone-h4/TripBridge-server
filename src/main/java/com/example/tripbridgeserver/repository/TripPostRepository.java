package com.example.tripbridgeserver.repository;

import com.example.tripbridgeserver.entity.TripPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripPostRepository  extends JpaRepository<TripPost,Long> {
}
