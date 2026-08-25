package com.dhoon.footmatch.teammember.exception.exceptions;

public class TeamLeaderCannotLeaveException extends RuntimeException {
    public TeamLeaderCannotLeaveException() {
        super("MEMBER / STAFF 만 탈퇴가 가능합니다.");
    }
}
