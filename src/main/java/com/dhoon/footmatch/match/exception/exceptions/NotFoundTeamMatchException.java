package com.dhoon.footmatch.match.exception.exceptions;

public class NotFoundTeamMatchException extends RuntimeException {
    public NotFoundTeamMatchException() {
        super("매치 조회 실패");
    }
}
