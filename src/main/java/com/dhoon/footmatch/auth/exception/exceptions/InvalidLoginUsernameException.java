package com.dhoon.footmatch.auth.exception.exceptions;

public class InvalidLoginUsernameException extends RuntimeException {
    public InvalidLoginUsernameException() {
        super("아이디가 올바르지 않습니다.");

    }
}
