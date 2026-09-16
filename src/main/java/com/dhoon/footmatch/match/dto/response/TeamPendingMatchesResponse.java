package com.dhoon.footmatch.match.dto.response;

import com.dhoon.footmatch.match.dto.request.TeamMatchCreateRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamPendingMatchesResponse {

    private List<TeamPendingMatchResponse> pendingMatches = new ArrayList<>();

    public static TeamPendingMatchesResponse of(List<TeamPendingMatchResponse> pendingMatches) {
        return new TeamPendingMatchesResponse(pendingMatches);
    }


}
