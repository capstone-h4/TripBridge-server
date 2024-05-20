package com.example.tripbridgeserver.repository;

import com.example.tripbridgeserver.entity.TripPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripPostRepository  extends JpaRepository<TripPost,Long> {
    //Trip 게시글 시간순서대로 정렬
    List<TripPost> findAllByOrderByCreatedAtDesc();
}
