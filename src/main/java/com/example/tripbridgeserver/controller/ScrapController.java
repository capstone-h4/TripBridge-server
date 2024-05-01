package com.example.tripbridgeserver.controller;

import com.example.tripbridgeserver.dto.ScrapDTO;
import com.example.tripbridgeserver.entity.Scrap;
import com.example.tripbridgeserver.entity.UserEntity;
import com.example.tripbridgeserver.repository.ScrapRepository;
import com.example.tripbridgeserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
public class ScrapController {

    // 장소 스크랩 추가, 삭제 기능
    private final ScrapRepository scrapRepository;
    private final UserRepository userRepository;

    @Autowired
    public ScrapController(ScrapRepository scrapRepository, UserRepository userRepository){
        this.scrapRepository = scrapRepository;
        this.userRepository = userRepository;
    }


    @PostMapping("/storage")
    public Scrap create(@RequestBody ScrapDTO dto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        UserEntity currentUser = userRepository.findByEmail(userEmail);
        Scrap scrap = dto.toEntity(currentUser);
        return scrapRepository.save(scrap);
    }

    @DeleteMapping("/storage/{id}")
    public ResponseEntity<Scrap> delete(@PathVariable Long id){
        Scrap target = scrapRepository.findById(id).orElse(null);
        if(target==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        scrapRepository.delete(target);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }



}
