package com.dhoon.footmatch.teamjoinrequest.exception.exceptions;

public class DuplicateTeamJoinRequestException extends RuntimeException {
    public DuplicateTeamJoinRequestException() {
        super("이미 해당팀에 가입신청이 있습니다.");
    }
}
