package com.example.tripbridgeserver.dto;

import com.example.tripbridgeserver.entity.MatePost;
import com.example.tripbridgeserver.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class UserDTO {

    private String name;
    private String nickname;
    private String email;
    private String pw;
    private String pw_check;
    private int alarm;


}
