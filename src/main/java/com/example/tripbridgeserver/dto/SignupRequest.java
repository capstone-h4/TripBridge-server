package com.example.tripbridgeserver.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class SignupRequest {

	private String id;
	private String name;
	private String nickname;
	private String email;
	private String password;
	private String pwCheck;
	private String alarm;
	private String alarm2;
	private String token;
}
