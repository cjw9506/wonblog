package com.wonblog.api.dto.response;

import com.wonblog.api.domain.Post;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentResponse {

    private final Long id;
    private final String content;
    private final String writer;
    private final Post post;

    @Builder
    public CommentResponse(Long id, String content, String writer, Post post) {
        this.id = id;
        this.content = content;
        this.writer = writer;
        this.post = post;
    }
}
