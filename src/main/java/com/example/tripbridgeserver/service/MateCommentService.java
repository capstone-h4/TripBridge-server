package com.example.tripbridgeserver.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.tripbridgeserver.dto.MateCommentRequest;
import com.example.tripbridgeserver.entity.MateComment;
import com.example.tripbridgeserver.entity.MatePost;
import com.example.tripbridgeserver.entity.User;
import com.example.tripbridgeserver.repository.MateCommentRepository;
import com.example.tripbridgeserver.repository.MatePostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MateCommentService {

	private final MatePostRepository matePostRepository;
	private final MateCommentRepository mateCommentRepository;

	public List<MateComment> getMateCommentByMatePost(Long matePostId) {
		MatePost matePost = matePostRepository.findById(matePostId).orElse(null);
		if (matePost != null){
			return mateCommentRepository.findByMatePost(matePost);
		} else {
			return null;
		}
	}

	public MateComment createMateComment(MateCommentRequest dto, User user) {
		MateComment mateComment = dto.toEntity(user, matePostRepository);

		if (dto.getParentCommentId() != null) { // 상위 계층의 댓글이 있는 경우
			MateComment parentComment = mateCommentRepository.findById(dto.getParentCommentId())
				.orElseThrow(() -> new RuntimeException("해당 상위 계층의 댓글을 찾지 못했습니다. 해당 댓글 Id: " + dto.getParentCommentId()));
			mateComment.setParentComment(parentComment);
			mateComment.setDepth(parentComment.getDepth() + 1);
			mateComment.setCommentGroup(parentComment.getCommentGroup());
		} else {
			Long maxCommentGroup = mateCommentRepository.findMaxCommentGroupByMatePostId(dto.getMatePostId());
			mateComment.setCommentGroup(maxCommentGroup != null ? maxCommentGroup + 1 : 0L);
		}

		Long maxOrder = mateCommentRepository.findMaxOrderOfComment(dto.getParentCommentId());
		if (maxOrder != null) {
			mateComment.setCommentOrder(maxOrder + 1);
		} else {
			mateComment.setCommentOrder(0L);
		}

		return mateCommentRepository.save(mateComment);
	}

	public MateComment updateMateComment(Long mateCommentId, MateCommentRequest dto, User user) {
		MateComment mateComment = dto.toEntity(user, matePostRepository);

		MateComment target = mateCommentRepository.findById(mateCommentId).orElse(null);
		if (target == null) {
			return null;
		}

		target.setMatePost(mateComment.getMatePost());
		target.setContent(mateComment.getContent());
		target.setUser(mateComment.getUser());

		return mateCommentRepository.save(target);
	}

	public boolean deleteMateComment(Long mateCommentId) {
		MateComment mateComment = mateCommentRepository.findById(mateCommentId).orElse(null);
		if (mateComment == null) {
			return false;
		}

		mateCommentRepository.delete(mateComment);

		return true;
	}
}
