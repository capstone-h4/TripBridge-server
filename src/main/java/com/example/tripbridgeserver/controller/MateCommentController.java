package com.example.tripbridgeserver.controller;

import com.example.tripbridgeserver.dto.MateCommentDTO;
import com.example.tripbridgeserver.dto.MatePostDTO;
import com.example.tripbridgeserver.entity.MateComment;
import com.example.tripbridgeserver.entity.MatePost;
import com.example.tripbridgeserver.repository.MateCommentRepository;
import com.example.tripbridgeserver.repository.MatePostRepository;
import com.example.tripbridgeserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class MateCommentController {

    private final MatePostRepository matePostRepository;
    private final UserRepository userRepository;
    private final MateCommentRepository mateCommentRepository;

    @Autowired
    public MateCommentController(MatePostRepository matePostRepository, UserRepository userRepository, MateCommentRepository mateCommentRepository) {
        this.matePostRepository = matePostRepository;
        this.userRepository = userRepository;
        this.mateCommentRepository = mateCommentRepository;
    }
    @GetMapping("/mate/comment")
        public List<MateComment> index(){
            return mateCommentRepository.findAll();
    }
    @GetMapping("/mate/comment/{id}")
    public MateComment show (@PathVariable Long id){
        return mateCommentRepository.findById(id).orElse(null);
    }

    /*@PostMapping("/mate/comment")
    public MateComment create(@RequestBody MateCommentDTO dto){
        MateComment mateComment = dto.toEntity(userRepository,matePostRepository);
        return mateCommentRepository.save(mateComment);
    }*/

    @PostMapping("/mate/comment")
    public ResponseEntity<MateComment> createComment(@RequestBody MateCommentDTO dto) {
        MateComment mateComment = dto.toEntity(userRepository, matePostRepository);

        // 부모 댓글이 있는 경우
        if (dto.getParent_comment_id() != null) {
            MateComment parentComment = mateCommentRepository.findById(dto.getParent_comment_id())
                    .orElseThrow(() -> new RuntimeException("Parent comment not found with id: " + dto.getParent_comment_id()));
            mateComment.setParentComment(parentComment);
            mateComment.setDepth(parentComment.getDepth() + 1);
            mateComment.setComment_group(parentComment.getComment_group());
        }
        else {
            Long maxCommentGroup = mateCommentRepository.findMaxCommentGroupByMatePostId(dto.getMatePost_id()); // 해당 게시물에서 가장 높은 comment_group 값을 가져옴
            mateComment.setComment_group(maxCommentGroup != null ? maxCommentGroup + 1 : 0L); // 새로운 댓글의 comment_group을 설정
        }

        // 대댓글의 순서 설정
        Long maxOrder = mateCommentRepository.findMaxOrderOfComment(dto.getParent_comment_id());
        if (maxOrder != null) {
            mateComment.setComment_order(maxOrder + 1);
        } else {
            mateComment.setComment_order(0L); // 대댓글이 부모 댓글의 첫 번째 대댓글일 경우
        }

        MateComment savedComment = mateCommentRepository.save(mateComment);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedComment);
    }


    @PatchMapping("/mate/comment/{id}")
    public ResponseEntity<MateComment> update(@PathVariable Long id, @RequestBody MateCommentDTO dto){
        MateComment mateComment = dto.toEntity(userRepository,matePostRepository);
        MateComment target = mateCommentRepository.findById(id).orElse(null);

        if(target==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        target.setMatePost(mateComment.getMatePost());
        target.setContent(mateComment.getContent());
        target.setUser(mateComment.getUser());
        MateComment updated = mateCommentRepository.save(target);
        return ResponseEntity.status(HttpStatus.OK).body(updated);

    }


    @DeleteMapping("/mate/comment/{id}")
    public ResponseEntity<MateComment> delete(@PathVariable Long id){
        MateComment target = mateCommentRepository.findById(id).orElse(null);
        if(target==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        mateCommentRepository.delete(target);
        return ResponseEntity.status(HttpStatus.OK).body(null);

    }
}
