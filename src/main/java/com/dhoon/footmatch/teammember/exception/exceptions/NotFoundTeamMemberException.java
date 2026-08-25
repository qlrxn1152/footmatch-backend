package com.dhoon.footmatch.teammember.exception.exceptions;

public class NotFoundTeamMemberException extends RuntimeException {
    public NotFoundTeamMemberException() {
        super("팀 멤버 조회 실패");
    }
}
