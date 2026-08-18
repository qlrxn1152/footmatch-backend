package com.dhoon.footmatch.teamjoinrequest.exception.exceptions;

public class TeamJoinRequestStatusException extends RuntimeException {
    public TeamJoinRequestStatusException() {
        super("PENDING 상태의 가입신청이 아닙니다.");
    }
}
