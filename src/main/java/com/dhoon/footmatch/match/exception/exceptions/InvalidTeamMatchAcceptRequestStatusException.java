package com.dhoon.footmatch.match.exception.exceptions;

public class InvalidTeamMatchAcceptRequestStatusException extends RuntimeException {
    public InvalidTeamMatchAcceptRequestStatusException() {
        super("매치 수락 요청의 상태가 PENDING 이 아닙니다.");
    }
}
