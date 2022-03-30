package com.potato.instock.exceptions.user;

public class UserInfoException extends RuntimeException {
    public UserInfoException(String message) {
        super(message);
    }

    public UserInfoException(String message, Throwable cause) {
        super(message, cause);
    }
}
