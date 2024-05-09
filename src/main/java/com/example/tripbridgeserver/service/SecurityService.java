package com.example.tripbridgeserver.service;

import com.example.tripbridgeserver.entity.MatePost;
import com.example.tripbridgeserver.entity.UserEntity;
import com.example.tripbridgeserver.repository.MatePostRepository;
import com.example.tripbridgeserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    private final MatePostRepository matePostRepository;
    private final UserRepository userRepository;

    @Autowired
    public SecurityService(MatePostRepository matePostRepository, UserRepository userRepository) {
        this.matePostRepository = matePostRepository;
        this.userRepository = userRepository;
    }

    public boolean isCurrentUser(Long postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        UserEntity currentUser = userRepository.findByEmail(userEmail);
        MatePost matePost = matePostRepository.findById(postId).orElse(null);
        return matePost != null && matePost.getUserEntity().getId().equals(currentUser.getId());
    }
}