package com.wonblog.api.exception;

public class AlreadyExistEmail extends WonBlogException{

    private static final String MESSAGE = "이미 존재하는 이메일입니다.";

    public AlreadyExistEmail() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
