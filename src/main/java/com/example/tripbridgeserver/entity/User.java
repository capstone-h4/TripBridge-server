package com.example.tripbridgeserver.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
    @AllArgsConstructor
    @Entity
    @Getter
    @Setter
    @Table(name = "user")
    public class User {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @Column
        private String name;
        @Column
        private String nickname;
        @Column
        private String pw;
        @Column
        private String pw_check;
        @Column
        private String alarm;


    }
