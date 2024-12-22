package com.example.tripbridgeserver.service;

import org.springframework.stereotype.Service;

import com.example.tripbridgeserver.dto.MatePostRequest;
import com.example.tripbridgeserver.entity.MatePost;
import com.example.tripbridgeserver.entity.User;
import com.example.tripbridgeserver.repository.MatePostRepository;
import com.example.tripbridgeserver.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MatePostService {

	private final MatePostRepository matePostRepository;
	private final UserRepository userRepository;

	public MatePost createMatePost(MatePostRequest matePostRequest, String userEmail) {
		User user = userRepository.findByEmail(userEmail);

		MatePost matePost = matePostRequest.toEntity(user);

		matePostRepository.save(matePost);

		return matePost;
	}

	public MatePost updateMatePost(Long id, MatePostRequest matePostRequest, String userEmail) {
		User user = userRepository.findByEmail(userEmail);

		MatePost matePost = matePostRequest.toEntity(user);
		MatePost target = matePostRepository.findById(id).orElseThrow(() ->
			new RuntimeException("해당 게시물을 찾지 못했습니다. 게시물 Id : " + id));

		target.setTitle(matePost.getTitle());
		target.setContent(matePost.getContent());
		target.setUser(matePost.getUser());

		return matePostRepository.save(target);
	}

	public void deleteMatePost(Long id) {
		MatePost matePost = matePostRepository.findById(id).orElseThrow(() ->
			new RuntimeException("해당 게시물을 찾지 못했습니다. 게시물 Id : " + id));

		matePostRepository.delete(matePost);
	}
}
