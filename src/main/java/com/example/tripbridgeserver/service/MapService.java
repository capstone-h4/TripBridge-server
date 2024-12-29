package com.example.tripbridgeserver.service;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.tripbridgeserver.entity.Scrap;
import com.example.tripbridgeserver.entity.User;
import com.example.tripbridgeserver.repository.ScrapRepository;
import com.example.tripbridgeserver.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MapService {

	private final ScrapRepository scrapRepository;
	private final UserRepository userRepository;

	public List<Scrap> getUserScrap() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userEmail = authentication.getName();
		User currentUser = userRepository.findByEmail(userEmail);
		List<Scrap> scraps = scrapRepository.findByUser(currentUser);
		return scraps;
	}
}
