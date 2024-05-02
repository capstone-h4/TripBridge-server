package com.example.tripbridgeserver.repository;

import com.example.tripbridgeserver.entity.MatePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatePostRepository extends JpaRepository<MatePost,Long>{
    List<MatePost> findAllByOrderByCreatedAtDesc();
}