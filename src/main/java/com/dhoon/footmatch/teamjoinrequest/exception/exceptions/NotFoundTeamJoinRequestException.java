package com.dhoon.footmatch.teamjoinrequest.exception.exceptions;

public class NotFoundTeamJoinRequestException extends RuntimeException {
    public NotFoundTeamJoinRequestException() {
        super("팀 가입신청 조회 실패");
    }
}
