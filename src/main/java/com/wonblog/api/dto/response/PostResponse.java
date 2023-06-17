package com.wonblog.api.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final String author;
    private final LocalDateTime createdAt;


    @Builder
    public PostResponse(Long id, String title, String content, String author, LocalDateTime createdAt) {
        this.id = id;
        this.title = title.substring(0, Math.min(title.length(), 10));
        this.content = content;
        this.author = author;
        this.createdAt = createdAt;
    }
}
