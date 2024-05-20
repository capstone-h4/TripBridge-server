package com.example.tripbridgeserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//chatBot 에서 사용할 message
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private String role;
    private String content;

}