package com.example.tripbridgeserver.controller;

import com.example.tripbridgeserver.dto.TripCommentDTO;
import com.example.tripbridgeserver.entity.*;
import com.example.tripbridgeserver.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

//Trip 게시판 댓글 관련 Controller
@RestController
public class TripCommentController {

    private final TripPostRepository tripPostRepository;
    private final UserRepository userRepository;
    private final TripCommentRepository tripCommentRepository;

    @Autowired
    public TripCommentController(TripPostRepository tripPostRepository, UserRepository userRepository, TripCommentRepository tripCommentRepository) {
        this.tripPostRepository = tripPostRepository;
        this.userRepository = userRepository;
        this.tripCommentRepository = tripCommentRepository;
    }

    //Trip 게시판 id번 글에 대한 댓글 조회
    @GetMapping("/trip/{id}/comment")
    public List<TripComment> comment (@PathVariable Long id){
        TripPost tripPost = tripPostRepository.findById(id).orElse(null);
        if (tripPost != null){
            return tripCommentRepository.findByTripPost(tripPost) ; }
        else {
            return null; // 또는 예외를 처리하거나 적절한 방법으로 처리
        }
    }
    //Trip 게시판 댓글 생성
    @PostMapping("/trip/comment")
    public ResponseEntity<TripComment> createComment(@RequestBody TripCommentDTO dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        UserEntity currentUser = userRepository.findByEmail(userEmail);
        TripComment tripComment = dto.toEntity(currentUser, tripPostRepository);

        // 부모 댓글이 있는 경우
        if (dto.getParent_comment_id() != null) {
            TripComment parentComment = tripCommentRepository.findById(dto.getParent_comment_id())
                    .orElseThrow(() -> new RuntimeException("Parent comment not found with id: " + dto.getParent_comment_id()));
            tripComment.setParentComment(parentComment);
            tripComment.setDepth(parentComment.getDepth() + 1);
            tripComment.setComment_group(parentComment.getComment_group());
        }
        else {
            Long maxCommentGroup = tripCommentRepository.findMaxCommentGroupByMatePostId(dto.getTripPost_id()); // 해당 게시물에서 가장 높은 comment_group 값을 가져옴
            tripComment.setComment_group(maxCommentGroup != null ? maxCommentGroup + 1 : 0L); // 새로운 댓글의 comment_group 을 설정
        }

        // 대댓글의 순서 설정
        Long maxOrder = tripCommentRepository.findMaxOrderOfComment(dto.getParent_comment_id());
        if (maxOrder != null) {
            tripComment.setComment_order(maxOrder + 1);
        } else {
            tripComment.setComment_order(0L); // 대댓글이 부모 댓글의 첫 번째 대댓글일 경우
        }
        TripComment savedComment = tripCommentRepository.save(tripComment);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedComment);
    }

    //Trip 게시판 단일 댓글 수정
    @PatchMapping("/trip/comment/{id}")
    public ResponseEntity<TripComment> update(@PathVariable Long id, @RequestBody TripCommentDTO dto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        UserEntity currentUser = userRepository.findByEmail(userEmail);
        TripComment tripComment = dto.toEntity(currentUser, tripPostRepository);

        TripComment target = tripCommentRepository.findById(id).orElse(null);

        if(target==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        target.setTripPost(tripComment.getTripPost());
        target.setContent(tripComment.getContent());
        target.setUserEntity(tripComment.getUserEntity());
        TripComment updated = tripCommentRepository.save(target);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }
    //Trip 게시판 단일 댓글 삭제
    @DeleteMapping("/trip/comment/{id}")
    public ResponseEntity<TripComment> delete(@PathVariable Long id){
        TripComment target = tripCommentRepository.findById(id).orElse(null);
        if(target==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        tripCommentRepository.delete(target);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
