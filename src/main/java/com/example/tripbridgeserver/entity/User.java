package com.example.tripbridgeserver.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 50)
    private String nickname;

    @Column(nullable = false, length = 50, unique = true)
    private String email;

    @Column(name = "pw", nullable = false, length = 20)
    private String password;

    @Column(name = "pw_check", nullable = false, length = 20)
    private String pw_check;

    @Column(nullable = false)
    private Integer alarm;

    @Column(nullable = false)
    private Integer alarm2;

    @Column(length = 255)
    private String token;
}
