package com.example.tripbridgeserver.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;


public class UserResponseDTO {

    // 로그인 시 응답
    @Builder
    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Login {
        String accessToken;
        String refreshToken;
        String nickname;
    }


}

