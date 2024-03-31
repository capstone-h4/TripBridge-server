package com.example.tripbridgeserver.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode
@Table(name = "user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "nickname", nullable = false, length = 50)
    private String nickname;

    @Column(name = "email", nullable = false, length = 50, unique = true)
    private String email;

    @Column(name = "pw", nullable = false, length = 20)
    private String password;

    @Column(name = "pw_check", nullable = false, length = 20)
    private String pw_check;

    @Column(name = "alarm", nullable = false)
    private Integer alarm;

    @Column(name = "alarm2", nullable = false)
    private Integer alarm2;

    @Column(name = "token", length = 255)
    private String token;

}