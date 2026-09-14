package com.dhoon.footmatch.match.exception.exceptions;

public class NotFoundTeamMatchAcceptRequestException extends RuntimeException {
    public NotFoundTeamMatchAcceptRequestException() {
        super("매치 수락 요청을 조회하지 못했습니다.");
    }
}
