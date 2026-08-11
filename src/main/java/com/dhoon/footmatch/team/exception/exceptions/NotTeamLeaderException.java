package com.dhoon.footmatch.team.exception.exceptions;

public class NotTeamLeaderException extends RuntimeException {
    public NotTeamLeaderException() {
        super("팀장이 아닙니다.");
    }

}
