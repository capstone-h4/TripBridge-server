package com.example.tripbridgeserver.dto;

import com.example.tripbridgeserver.entity.MatePost;
import com.example.tripbridgeserver.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class MatePostDTO {
    private String title;
    private String content;

    public MatePost toEntity(UserEntity currentUser) {
        MatePost matePost = new MatePost();
        matePost.setTitle(this.title);
        matePost.setContent(this.content);
        matePost.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        matePost.setUserEntity(currentUser);
        return matePost;
    }
}
