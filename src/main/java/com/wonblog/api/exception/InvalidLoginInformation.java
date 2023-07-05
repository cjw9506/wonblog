package com.wonblog.api.exception;

public class InvalidLoginInformation extends WonBlogException{

    private static final String MESSAGE = "이메일/비밀번호가 올바르지 않습니다.";

    public InvalidLoginInformation() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
