package com.dhoon.footmatch.team.exception.exceptions;

public class DuplicateTeamNameException extends RuntimeException {
    public DuplicateTeamNameException() {
        super("해당 팀 이름이 이미 존재합니다.");
    }
}
