package com.dhoon.footmatch.match.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamMatchPendingListResponse {

    private List<TeamMatchPendingResponse> pendingMatches = new ArrayList<>();

    public static TeamMatchPendingListResponse of(List<TeamMatchPendingResponse> pendingMatches) {
        return new TeamMatchPendingListResponse(pendingMatches);
    }
}
