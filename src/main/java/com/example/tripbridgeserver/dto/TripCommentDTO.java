package com.example.tripbridgeserver.dto;

import com.example.tripbridgeserver.entity.*;
import com.example.tripbridgeserver.repository.MatePostRepository;
import com.example.tripbridgeserver.repository.TripPostRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class TripCommentDTO {
    private Long tripPost_id;
    private String content;
    private Long parent_comment_id;

    private  final TripPostRepository tripPostRepository;

    public TripComment toEntity(UserEntity currentUser, TripPostRepository tripPostRepository ){
        TripPost tripPost = tripPostRepository.findById(tripPost_id)
                .orElseThrow(() -> new RuntimeException("TripPest not found with id: " + tripPost_id));
        TripComment tripComment = new TripComment();
        tripComment.setContent(this.content);
        tripComment.setCreated_at(new Timestamp(System.currentTimeMillis()));
        tripComment.setUserEntity(currentUser);
        tripComment.setTripPost(tripPost);
        tripComment.setDepth(0L); // 대댓글의 깊이는 일단 0으로 설정
        tripComment.setComment_group(0L); // 대댓글의 그룹은 일단 0으로 설정
        tripComment.setComment_order(0L); // 대댓글의 순서는 일단 0으로 설정
        return tripComment;
    }
}