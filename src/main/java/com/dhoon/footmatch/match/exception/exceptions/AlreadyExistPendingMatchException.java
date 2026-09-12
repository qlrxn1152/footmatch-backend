package com.dhoon.footmatch.match.exception.exceptions;

public class AlreadyExistPendingMatchException extends RuntimeException {
    public AlreadyExistPendingMatchException() {
        super("이미 PENDING 매치가 존재합니다.");
    }
}
