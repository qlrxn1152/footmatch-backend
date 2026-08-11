package com.dhoon.footmatch.teammember.validation.exceptions;

public class NotJoinedTeamException extends RuntimeException {
    public NotJoinedTeamException() {
        super("팀에 속해있지 않은 회원입니다.");
    }
}
