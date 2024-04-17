package com.example.tripbridgeserver.controller;


import com.example.tripbridgeserver.entity.Scrap;
import com.example.tripbridgeserver.entity.UserEntity;
import com.example.tripbridgeserver.repository.ScrapRepository;
import com.example.tripbridgeserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MapController {

    private final ScrapRepository scrapRepository;
    private final UserRepository userRepository;

    @Autowired
    public MapController(ScrapRepository scrapRepository, UserRepository userRepository) {
        this.scrapRepository = scrapRepository;
        this.userRepository = userRepository;
    }
    @GetMapping("/scrap")
    public List<Scrap> show(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        UserEntity currentUser = userRepository.findByEmail(userEmail);

        List<Scrap> scraps = scrapRepository.findByUserEntity(currentUser);
        return scraps;
    }
}
