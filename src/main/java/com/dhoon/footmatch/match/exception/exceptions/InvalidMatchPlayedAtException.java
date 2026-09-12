package com.dhoon.footmatch.match.exception.exceptions;

public class InvalidMatchPlayedAtException extends RuntimeException {
    public InvalidMatchPlayedAtException() {
        super("잘못된 형식의 매치 시간입니다.");
    }
}
