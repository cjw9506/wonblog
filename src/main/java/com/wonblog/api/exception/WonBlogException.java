package com.wonblog.api.exception;

public abstract class WonBlogException extends RuntimeException{

    public WonBlogException(String message) {
        super(message);
    }

    public WonBlogException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatusCode();
}
