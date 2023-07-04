package com.wonblog.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Signup {

    @NotBlank(message = "이메일을 입력하세요.")
    private String email;

    @NotBlank(message = "이름을 입력하세요.")
    private String username;

    @NotBlank(message = "비밀번호를 입력하세요.")
    private String password;

    @Builder
    public Signup(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }
}
