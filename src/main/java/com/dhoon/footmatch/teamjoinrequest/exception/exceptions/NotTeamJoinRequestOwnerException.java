package com.dhoon.footmatch.teamjoinrequest.exception.exceptions;

public class NotTeamJoinRequestOwnerException extends RuntimeException {
    public NotTeamJoinRequestOwnerException() {
        super("신청자가 아닙니다.");
    }
}
