package com.dhoon.footmatch.teammember.exception.exceptions;

public class NotTeamMemberException extends RuntimeException {
    public NotTeamMemberException() {
        super("해당팀의 멤버가 아닙니다.");
    }
}
