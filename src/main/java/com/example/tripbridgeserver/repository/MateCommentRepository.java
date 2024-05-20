package com.example.tripbridgeserver.repository;

import com.example.tripbridgeserver.entity.MateComment;
import com.example.tripbridgeserver.entity.MatePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface MateCommentRepository extends JpaRepository<MateComment,Long> {

    // 대댓글 순서 설정시 부모 댓글에 대한 현재 댓글의 order 값
    @Query("SELECT MAX(c.comment_order) FROM MateComment c WHERE c.parentComment.id = :parentId")
    Long findMaxOrderOfComment(@Param("parentId") Long parentId);

    // 해당 게시물에서 가장 높은 comment_group 값
    @Query("SELECT COALESCE(MAX(c.comment_group), 0) FROM MateComment c WHERE c.matePost.id = :matePostId AND c.parentComment IS NULL")
    Long findMaxCommentGroupByMatePostId(@Param("matePostId") Long matePostId);

    List<MateComment> findByMatePost(MatePost matePost);

}
