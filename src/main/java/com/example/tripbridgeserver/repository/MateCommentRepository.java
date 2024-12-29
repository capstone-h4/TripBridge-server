package com.example.tripbridgeserver.repository;

import com.example.tripbridgeserver.entity.MateComment;
import com.example.tripbridgeserver.entity.MatePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface MateCommentRepository extends JpaRepository<MateComment,Long> {
    @Query("SELECT MAX(c.commentOrder) FROM MateComment c WHERE c.parentComment.id = :parentId")
    Long findMaxOrderOfComment(@Param("parentId") Long parentId);

    @Query("SELECT COALESCE(MAX(c.commentGroup), 0) FROM MateComment c WHERE c.matePost.id = :matePostId AND c.parentComment IS NULL")
    Long findMaxCommentGroupByMatePostId(@Param("matePostId") Long matePostId);

    List<MateComment> findByMatePost(MatePost matePost);
}
