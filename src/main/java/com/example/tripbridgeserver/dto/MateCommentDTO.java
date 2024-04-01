package com.example.tripbridgeserver.dto;

import com.example.tripbridgeserver.entity.MateComment;
import com.example.tripbridgeserver.entity.MatePost;
import com.example.tripbridgeserver.entity.User;
import com.example.tripbridgeserver.repository.MatePostRepository;
import com.example.tripbridgeserver.repository.UserRepository2;
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
    private Long user_id;
    private Long parent_comment_id;

    private final UserRepository2 userRepository2;
    private  final MatePostRepository matePostRepository;

    public MateComment toEntity(UserRepository2 userRepository2, MatePostRepository matePostRepository ){
        User user = userRepository2.findById(user_id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + user_id));
        MatePost matePost = matePostRepository.findById(matePost_id)
                .orElseThrow(() -> new RuntimeException("MatePest not found with id: " + matePost_id));
        MateComment mateComment = new MateComment();
        mateComment.setContent(this.content);
        mateComment.setCreated_at(new Timestamp(System.currentTimeMillis()));
        mateComment.setUser(user);
        mateComment.setMatePost(matePost);
        mateComment.setDepth(0L); // 대댓글의 깊이는 일단 0으로 설정
        mateComment.setComment_group(0L); // 대댓글의 그룹은 일단 0으로 설정
        mateComment.setComment_order(0L); // 대댓글의 순서는 일단 0으로 설정
        return mateComment;

    }





}
