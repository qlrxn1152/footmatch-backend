package com.dhoon.footmatch.team.exception.exceptions;

public class SameTeamNameException extends RuntimeException {
    public SameTeamNameException() {
        super("팀 이름 변경사항이 없습니다.");
    }
}
