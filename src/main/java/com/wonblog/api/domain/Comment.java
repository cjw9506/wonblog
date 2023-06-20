package com.wonblog.api.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Comment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Lob
    private String content; //내용

    private String writer; //작성자

    private LocalDateTime createdAt; //작성일

    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post; //게시글

    @Builder
    public Comment(String content, String writer, Post post) {
        this.content = content;
        this.writer = writer;
        this.post = post;
        this.createdAt = LocalDateTime.now();
    }

    public void change(String content, String writer, LocalDateTime updatedAt) {
        this.content = content;
        this.writer = writer;
        this.updatedAt = updatedAt;
    }

    public void updatePost(Post post) {
        this.post = post;
    }
}
