package com.dhoon.footmatch.match.dto.response;

import com.dhoon.footmatch.match.domain.TeamMatch;
import com.dhoon.footmatch.match.domain.TeamMatchAcceptRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamMatchAcceptRequestResponse {

    private Long requestId;
    private Long matchId;
    private String homeTeamName;
    private String awayTeamName;

    public static TeamMatchAcceptRequestResponse of(TeamMatch teamMatch, TeamMatchAcceptRequest teamMatchAcceptRequest) {
        return new TeamMatchAcceptRequestResponse(
                teamMatchAcceptRequest.getId(),
                teamMatch.getId(),
                teamMatch.getHomeTeam().getTeamName(),
                teamMatch.getAwayTeam().getTeamName()
        );
    }
}
