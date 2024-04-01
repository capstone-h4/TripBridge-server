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
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "matePost_id")
    private  MatePost matePost;

    @Column(name = "content", nullable = false)
    private String content;

    @CreatedDate
    @Column(name="created_at",updatable = false)
    private Timestamp created_at;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="parent_comment_id")
    private MateComment parentComment;

    @Column(name = "depth", nullable = false)
    private Long depth;

    @Column(name = "comment_order", nullable = false)
    private Long comment_order;

    @Column(name = "comment_group", nullable = false)
    private Long comment_group;

}
