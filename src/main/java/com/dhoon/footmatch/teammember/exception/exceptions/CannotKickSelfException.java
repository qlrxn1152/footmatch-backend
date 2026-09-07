package com.dhoon.footmatch.teammember.exception.exceptions;

public class CannotKickSelfException extends RuntimeException {
    public CannotKickSelfException() {
        super("자기 자신을 강퇴할 수 없습니다.");
    }
}
