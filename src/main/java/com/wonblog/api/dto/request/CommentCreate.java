package com.wonblog.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
public class CommentCreate {

    @NotBlank(message = "내용을 입력하세요.")
    private String content;

    @NotBlank(message = "작성자를 입력하세요.")
    private String writer;

    @Builder
    public CommentCreate(String content, String writer) {
        this.content = content;
        this.writer = writer;
    }
}
