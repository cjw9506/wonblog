package com.wonblog.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    private String title;//제목

    @Lob
    private String content;//내용

    private String author;//작성자

    private LocalDateTime createdAt;//생성일

    private LocalDateTime updatedAt;//수정일

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private List<Comment> comments = new ArrayList<>();

   @Builder
    public Post(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.createdAt = LocalDateTime.now();
    }

    public void change(String title, String content, String author, LocalDateTime updatedAt) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.updatedAt = updatedAt;
    }

    public void addComment(Comment comment) {
       this.getComments().add(comment);
       comment.updatePost(this);
    }
}
