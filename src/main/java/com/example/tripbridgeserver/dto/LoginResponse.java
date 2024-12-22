package com.example.tripbridgeserver.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
@Getter
public class LoginResponse {
	private String accessToken;
	private String refreshToken;
	private String nickname;
}
