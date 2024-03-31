package com.example.tripbridgeserver.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

// 사용자 관련 요청
public class UserRequestDTO {

    // 회원가입
    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class SignUp {
        String id;
        String name;
        String nickname;
        String email;
        String password;
        String pw_check;
        String alarm;
        String alarm2;
        String token;
    }

    // 로그인
    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Login {
        String email;
        String password;
    }


}
