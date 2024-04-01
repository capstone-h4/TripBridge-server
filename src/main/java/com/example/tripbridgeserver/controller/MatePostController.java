package com.example.tripbridgeserver.controller;

import com.example.tripbridgeserver.dto.MatePostDTO;
import com.example.tripbridgeserver.entity.MatePost;
import com.example.tripbridgeserver.entity.User;
import com.example.tripbridgeserver.repository.MatePostRepository;
import com.example.tripbridgeserver.repository.UserRepository2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MatePostController {

    private final MatePostRepository matePostRepository;
    private final UserRepository2 userRepository2;

    @Autowired
    public MatePostController(MatePostRepository matePostRepository, UserRepository2 userRepository2) {
        this.matePostRepository = matePostRepository;
        this.userRepository2 = userRepository2;
    }
    @GetMapping("/user")
    public List<User> index1(){
        return userRepository2.findAll();
    }
    @GetMapping("/user/{id}")
    public User show1 (@PathVariable Long id){
        return userRepository2.findById(id).orElse(null);
    }



    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @GetMapping("/mate")
    public List<MatePost> index(){
        return matePostRepository.findAll();
    }

    @GetMapping("/mate/{id}")
    public MatePost show (@PathVariable Long id){
        return matePostRepository.findById(id).orElse(null);
    }

    @PostMapping("/mate")
    public MatePost create(@RequestBody  MatePostDTO dto){
        MatePost matePost=dto.toEntity(userRepository2);
        return matePostRepository.save(matePost);

    }

    @PatchMapping("/mate/{id}")
    public ResponseEntity<MatePost> update(@PathVariable Long id, @RequestBody MatePostDTO dto){
        MatePost matePost= dto.toEntity(userRepository2);
        MatePost target = matePostRepository.findById(id).orElse(null);

        if(target==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        target.setTitle(matePost.getTitle());
        target.setContent(matePost.getContent());
        target.setUser(matePost.getUser());
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
