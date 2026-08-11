package com.dhoon.footmatch.team.exception.exceptions;

public class NotFoundTeamException extends RuntimeException {
    public NotFoundTeamException() {
        super("팀 조회 실패");
    }
}
