package com.example.tripbridgeserver.repository;

import com.example.tripbridgeserver.entity.MateComment;
import com.example.tripbridgeserver.entity.MatePost;
import com.example.tripbridgeserver.entity.TripComment;
import com.example.tripbridgeserver.entity.TripPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TripCommentRepository extends JpaRepository<TripComment,Long> {

    @Query("SELECT MAX(c.comment_order) FROM TripComment c WHERE c.parentComment.id = :parentId")
    Long findMaxOrderOfComment(@Param("parentId") Long parentId);

    @Query("SELECT COALESCE(MAX(c.comment_group), 0) FROM TripComment c WHERE c.tripPost.id = :tripPostId AND c.parentComment IS NULL")
    Long findMaxCommentGroupByMatePostId(@Param("tripPostId") Long tripPostId);

    List<TripComment> findByTripPost(TripPost tripPost);
}
