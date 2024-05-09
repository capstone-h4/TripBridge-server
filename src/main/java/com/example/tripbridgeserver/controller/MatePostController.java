package com.example.tripbridgeserver.controller;

import com.example.tripbridgeserver.dto.MatePostDTO;
import com.example.tripbridgeserver.entity.MatePost;
import com.example.tripbridgeserver.entity.UserEntity;
import com.example.tripbridgeserver.repository.MatePostRepository;
import com.example.tripbridgeserver.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MatePostController {

    private final MatePostRepository matePostRepository;
    private final UserRepository userRepository;

    @Autowired
    public MatePostController(MatePostRepository matePostRepository, UserRepository userRepository) {
        this.matePostRepository = matePostRepository;
        this.userRepository = userRepository;
    }
    @GetMapping("/user")
    public List<UserEntity> index1(){
        return userRepository.findAll();
    }
    @GetMapping("/user/{id}")
    public UserEntity show1 (@PathVariable Long id){
        return userRepository.findById(id).orElse(null);
    }



    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @GetMapping("/mate")
    public List<MatePost> index(){

        return matePostRepository.findAllByOrderByCreatedAtDesc();
    }

    @GetMapping("/mate/{id}")
    public MatePost show (@PathVariable Long id){
        return matePostRepository.findById(id).orElse(null);
    }

    @PostMapping("/mate")
    public MatePost create(@RequestBody  MatePostDTO dto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        UserEntity currentUser = userRepository.findByEmail(userEmail);
        MatePost matePost=dto.toEntity(currentUser);
        return matePostRepository.save(matePost);

    }

    @PatchMapping("/mate/{id}")
    public ResponseEntity<MatePost> update(@PathVariable Long id, @RequestBody MatePostDTO dto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        UserEntity currentUser = userRepository.findByEmail(userEmail);
        MatePost matePost= dto.toEntity(currentUser);
        MatePost target = matePostRepository.findById(id).orElse(null);

        if(target==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        target.setTitle(matePost.getTitle());
        target.setContent(matePost.getContent());
        target.setUserEntity(matePost.getUserEntity());
        MatePost updated = matePostRepository.save(target);


        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @DeleteMapping("/mate/{id}")
    public ResponseEntity<MatePost> delete(@PathVariable Long id){
        MatePost target = matePostRepository.findById(id).orElse(null);
        if(target==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        matePostRepository.delete(target);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

}
