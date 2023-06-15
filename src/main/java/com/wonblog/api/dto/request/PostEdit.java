package com.wonblog.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
public class PostEdit {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @NotBlank(message = "작성자를 입력해주세요.")
    private String author;

    @Builder
    public PostEdit(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }
}
