package com.dhoon.footmatch.teamjoinrequest.exception.exceptions;

public class NotTeamJoinRequestException extends RuntimeException {
    public NotTeamJoinRequestException() {
        super("해당 팀의 가입신청이 아닙니다.");
    }
}
