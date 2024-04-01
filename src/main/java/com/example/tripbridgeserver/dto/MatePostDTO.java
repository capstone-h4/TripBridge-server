package com.example.tripbridgeserver.dto;

import com.example.tripbridgeserver.entity.MatePost;
import com.example.tripbridgeserver.entity.User;
import com.example.tripbridgeserver.repository.UserRepository1;
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
    private Long user_id;

    private final UserRepository1 userRepository1;



    public MatePost toEntity(UserRepository1 userRepository1) {
        User user = userRepository1.findById(user_id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + user_id));

        MatePost matePost = new MatePost();
        matePost.setTitle(this.title);
        matePost.setContent(this.content);
        matePost.setCreated_at(new Timestamp(System.currentTimeMillis()));
        matePost.setUser(user); // User 엔티티 객체를 MatePost 엔티티의 user 필드에 설정
        return matePost;
    }
}
