package com.dhoon.footmatch.team.exception.exceptions;

public class SameTeamLeaderException extends RuntimeException {
    public SameTeamLeaderException() {
        super("현재 팀장과 새로운 팀장이 동일합니다.");
    }
}
