package com.dhoon.footmatch.team.exception.exceptions;

public class AlreadyJoinedTeamException extends RuntimeException {
    public AlreadyJoinedTeamException() {
        super("팀에 이미 속한 회원입니다.");
    }
}
