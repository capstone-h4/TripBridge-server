package com.example.tripbridgeserver.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.tripbridgeserver.dto.TripCommentRequest;
import com.example.tripbridgeserver.entity.TripComment;
import com.example.tripbridgeserver.entity.TripPost;
import com.example.tripbridgeserver.entity.User;
import com.example.tripbridgeserver.repository.TripCommentRepository;
import com.example.tripbridgeserver.repository.TripPostRepository;
import com.example.tripbridgeserver.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TripCommentService {

	private final TripPostRepository tripPostRepository;
	private final UserRepository userRepository;
	private final TripCommentRepository tripCommentRepository;

	public List<TripComment> getTripCommentByTripPost(Long tripPostId) {
		TripPost tripPost = tripPostRepository.findById(tripPostId).orElse(null);
		if (tripPost != null){
			return tripCommentRepository.findByTripPost(tripPost);
		} else {
			return null;
		}
	} 

	public TripComment createTripComment(TripCommentRequest tripCommentRequest, String userEmail) {
		User user = userRepository.findByEmail(userEmail);

		TripComment tripComment = tripCommentRequest.toEntity(user, tripPostRepository);

		if (tripCommentRequest.getParentCommentId() != null) { // 상위 계층의 댓글이 있는 경우
			TripComment parentComment = tripCommentRepository.findById(tripCommentRequest.getParentCommentId())
				.orElseThrow(() -> new RuntimeException("해당 상위 계층의 댓글을 찾지 못했습니다. 해당 댓글 Id: " + tripCommentRequest.getParentCommentId()));
			tripComment.setParentComment(parentComment);
			tripComment.setDepth(parentComment.getDepth() + 1);
			tripComment.setCommentGroup(parentComment.getCommentGroup());
		}
		else {
			Long maxCommentGroup = tripCommentRepository
				.findMaxCommentGroupByTripPostId(tripCommentRequest.getTripPostId()); // 해당 게시물에서 가장 높은 commentGroup 값을 가져옴
			tripComment.setCommentGroup(maxCommentGroup != null ? maxCommentGroup + 1 : 0L); // 새로운 댓글의 commentGroup 을 설정
		}

		// 순서 설정
		Long maxOrder = tripCommentRepository.findMaxOrderOfComment(tripCommentRequest.getParentCommentId());
		if (maxOrder != null) {
			tripComment.setCommentOrder(maxOrder + 1);
		} else {
			tripComment.setCommentOrder(0L); // 대댓글이 상위 계층 댓글의 첫번째 대댓글일 경우
		}

		return tripCommentRepository.save(tripComment);
	}

	public TripComment updateTripComment(Long tripCommentId, TripCommentRequest tripCommentRequest, String userEmail) {
		User user = userRepository.findByEmail(userEmail);

		TripComment tripComment = tripCommentRequest.toEntity(user, tripPostRepository);

		TripComment target = tripCommentRepository.findById(tripCommentId).orElse(null);
		if(target == null){
			return null;
		}

		target.setTripPost(tripComment.getTripPost());
		target.setContent(tripComment.getContent());
		target.setUser(tripComment.getUser());

		return tripCommentRepository.save(target);
	}

	public TripComment deleteTripComment(Long tripCommentId) {
		TripComment tripComment = tripCommentRepository.findById(tripCommentId).orElse(null);
		if(tripComment == null){
			return null;
		}

		tripCommentRepository.delete(tripComment);
		
		return tripComment;
	}
}
