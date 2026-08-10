package com.dhoon.footmatch.member.exception.exceptions;

public class DuplicateUsernameException extends RuntimeException {
    public DuplicateUsernameException() {
        super("아이디 중복");
    }
}
