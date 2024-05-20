package com.example.tripbridgeserver.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import java.sql.Timestamp;
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "matePost")

public class MatePost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @CreatedDate
    @Column(name = "createdAt", updatable = false)
    private Timestamp createdAt;

    @ManyToOne
    @JoinColumn(name="user_id")
    private UserEntity userEntity;



}
