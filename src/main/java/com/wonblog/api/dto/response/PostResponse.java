package com.wonblog.api.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final String author;


    @Builder
    public PostResponse(Long id, String title, String content, String author) {
        this.id = id;
        this.title = title.substring(0, Math.min(title.length(), 10));
        this.content = content;
        this.author = author;
    }
}
