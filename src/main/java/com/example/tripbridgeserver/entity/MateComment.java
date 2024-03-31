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
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mateComment")
public class MateComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "matePost_id")
    private  MatePost matePost;

    @Column
    private String content;

    @CreatedDate
    @Column(updatable = false)
    private Timestamp created_at;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="parent_comment_id")
    private MateComment parentComment;

    @Column
    private Long depth;

    @Column
    private Long comment_order;

    @Column
    private Long comment_group;

}
