package com.dhoon.footmatch.match.exception.exceptions;

public class CannotRequestOwnTeamMatchException extends RuntimeException {
    public CannotRequestOwnTeamMatchException() {
        super("자신의 팀이 생성한 매치에는 수락 요청을 보낼 수 없습니다.");
    }
}
