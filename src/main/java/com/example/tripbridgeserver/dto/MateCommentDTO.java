package com.example.tripbridgeserver.dto;

import com.example.tripbridgeserver.entity.MateComment;
import com.example.tripbridgeserver.entity.MatePost;
import com.example.tripbridgeserver.entity.UserEntity;
import com.example.tripbridgeserver.repository.MatePostRepository;
import com.example.tripbridgeserver.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class MateCommentDTO {
    private Long matePost_id;
    private String content;
    private Long parent_comment_id;

    private  final MatePostRepository matePostRepository;

    public MateComment toEntity(UserEntity currentUser, MatePostRepository matePostRepository ){
        MatePost matePost = matePostRepository.findById(matePost_id)
                .orElseThrow(() -> new RuntimeException("MatePest not found with id: " + matePost_id));
        MateComment mateComment = new MateComment();
        mateComment.setContent(this.content);
        mateComment.setCreated_at(new Timestamp(System.currentTimeMillis()));
        mateComment.setUserEntity(currentUser);
        mateComment.setMatePost(matePost);
        mateComment.setDepth(0L); // 대댓글의 깊이는 일단 0으로 설정
        mateComment.setComment_group(0L); // 대댓글의 그룹은 일단 0으로 설정
        mateComment.setComment_order(0L); // 대댓글의 순서는 일단 0으로 설정
        return mateComment;

    }





}
