package com.dhoon.footmatch.team.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamLeaderTransferRequest {

    @NotNull
    private Long targetMemberId;

    public static TeamLeaderTransferRequest of(Long targetMemberId) {
        return new TeamLeaderTransferRequest(targetMemberId);
    }
}
