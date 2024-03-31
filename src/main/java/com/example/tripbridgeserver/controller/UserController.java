package com.example.tripbridgeserver.controller;

import com.example.tripbridgeserver.dto.UserRequestDTO;
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

    // 회원가입
    @PostMapping("/signup")
    public ResponseDTO<?> signUp(@RequestBody UserRequestDTO.SignUp dto) {
        return userService.signup(dto);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseDTO<?> login(@RequestBody UserRequestDTO.Login dto) {
        return userService.login(dto);
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseDTO<?> logout() {
        return userService.logout();
    }

}
