package com.example.tripbridgeserver.dto;

import com.example.tripbridgeserver.entity.*;
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
public class TripCommentRequest {

    private final TripPostRepository tripPostRepository;

    private Long tripPostId;
    private String content;
    private Long parentCommentId;

    public TripComment toEntity(User currentUser, TripPostRepository tripPostRepository ){
        TripPost tripPost = tripPostRepository.findById(tripPostId)
                .orElseThrow(() -> new RuntimeException("해당 게시글을 찾을 수 없습니다. TripPostId: " + tripPostId));
        TripComment tripComment = new TripComment();
        tripComment.setContent(this.content);
        tripComment.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        tripComment.setUser(currentUser);
        tripComment.setTripPost(tripPost);
        tripComment.setDepth(0L); // 계층 깊이
        tripComment.setCommentGroup(0L); // 그룹
        tripComment.setCommentOrder(0L); // 순서
        return tripComment;
    }
}
