package com.dhoon.footmatch.match.exception.exceptions;

public class NotPendingTeamMatchException extends RuntimeException {
    public NotPendingTeamMatchException() {
        super("PENDING 상태의 매치가 아닙니다.");
    }
}
