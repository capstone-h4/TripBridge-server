package com.example.tripbridgeserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    @GetMapping("/")
    public String home() {
        return "Hello World!";
    }

    @GetMapping("signup")
    public String singup(){
        return "회원가입";
    }


}
