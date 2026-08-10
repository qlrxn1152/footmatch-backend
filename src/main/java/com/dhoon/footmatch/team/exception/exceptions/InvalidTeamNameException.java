package com.dhoon.footmatch.team.exception.exceptions;

public class InvalidTeamNameException extends RuntimeException {
    public InvalidTeamNameException() {
        super("팀 이름은 2~20자 까지만 허용합니다.");
    }
}
