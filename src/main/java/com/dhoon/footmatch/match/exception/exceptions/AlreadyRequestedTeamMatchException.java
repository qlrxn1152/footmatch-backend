package com.dhoon.footmatch.match.exception.exceptions;

public class AlreadyRequestedTeamMatchException extends RuntimeException {
    public AlreadyRequestedTeamMatchException() {
        super("이미 해당 매치에 수락 요청을 보냈습니다.");
    }
}
