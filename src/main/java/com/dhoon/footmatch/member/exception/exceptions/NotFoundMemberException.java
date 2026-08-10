package com.dhoon.footmatch.member.exception.exceptions;

public class NotFoundMemberException extends RuntimeException {
    public NotFoundMemberException() {
        super("멤버 조회 실패");
    }
}
