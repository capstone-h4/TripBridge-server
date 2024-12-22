package com.example.tripbridgeserver.controller;

import com.example.tripbridgeserver.dto.LoginRequest;
import com.example.tripbridgeserver.dto.SignupRequest;
import com.example.tripbridgeserver.service.UserService;
import com.example.tripbridgeserver.common.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseDTO<?> signUp(@RequestBody SignupRequest signupRequest) {
        return userService.signup(signupRequest);
    }

    @PostMapping("/login")
    public ResponseDTO<?> login(@RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest);
    }

    @PostMapping("/logout")
    public ResponseDTO<?> logout() {
        return userService.logout();
    }
}
