package com.wonblog.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class PostCreate {

    @NotBlank(message = "제목을 입력하세요.")
    private String title;

    @NotBlank(message = "내용을 입력하세요.")
    private String content;

    @NotBlank(message = "작성자를 입력하세요.")
    private String author;

    @Builder
    public PostCreate(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public PostCreate change(String title, String content, String author) {
        return PostCreate.builder()
                .title(title)
                .content(content)
                .author(author)
                .build();
    }

}
