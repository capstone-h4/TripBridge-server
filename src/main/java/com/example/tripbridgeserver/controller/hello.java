package com.example.tripbridgeserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class hello {
    @GetMapping("/")
    public String test() {
        return "Hello World!";
    }

    @GetMapping("signup")
    public String singup(){
        return "회원가입";
    }


}
