package com.example.tripbridgeserver.dto;

import com.example.tripbridgeserver.entity.MateComment;
import com.example.tripbridgeserver.entity.MatePost;
import com.example.tripbridgeserver.entity.User;
import com.example.tripbridgeserver.repository.MatePostRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class MateCommentRequest {

    private final MatePostRepository matePostRepository;

    private Long matePostId;
    private String content;
    private Long parentCommentId;

    public MateComment toEntity(User currentUser, MatePostRepository matePostRepository ){
        MatePost matePost = matePostRepository.findById(matePostId)
                .orElseThrow(() -> new RuntimeException("해당 게시글을 찾을 수 없습니다. MatePostId: " + matePostId));
        MateComment mateComment = new MateComment();
        mateComment.setContent(this.content);
        mateComment.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        mateComment.setUser(currentUser);
        mateComment.setMatePost(matePost);
        mateComment.setDepth(0L); // 계층 깊이
        mateComment.setCommentGroup(0L); // 그룹
        mateComment.setCommentOrder(0L); // 순서
        return mateComment;
    }
}
