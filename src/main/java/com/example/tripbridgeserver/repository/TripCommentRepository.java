package com.example.tripbridgeserver.repository;

import com.example.tripbridgeserver.entity.TripComment;
import com.example.tripbridgeserver.entity.TripPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface TripCommentRepository extends JpaRepository<TripComment,Long> {

    // 대댓글 순서 설정시 부모 댓글에 대한 현재 댓글의 order 값
    @Query("SELECT MAX(c.comment_order) FROM TripComment c WHERE c.parentComment.id = :parentId")
    Long findMaxOrderOfComment(@Param("parentId") Long parentId);

    // 해당 게시물에서 가장 높은 comment_group 값
    @Query("SELECT COALESCE(MAX(c.comment_group), 0) FROM TripComment c WHERE c.tripPost.id = :tripPostId AND c.parentComment IS NULL")
    Long findMaxCommentGroupByMatePostId(@Param("tripPostId") Long tripPostId);

    List<TripComment> findByTripPost(TripPost tripPost);
}
