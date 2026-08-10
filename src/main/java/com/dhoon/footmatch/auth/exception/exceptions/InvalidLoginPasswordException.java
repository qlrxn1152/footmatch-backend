package com.dhoon.footmatch.auth.exception.exceptions;

public class InvalidLoginPasswordException extends RuntimeException {
    public InvalidLoginPasswordException() {
        super("비밀번호가 올바르지 않습니다.");
    }
}
