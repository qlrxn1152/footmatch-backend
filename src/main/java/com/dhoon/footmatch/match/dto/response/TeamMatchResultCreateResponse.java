package com.dhoon.footmatch.match.dto.response;

import com.dhoon.footmatch.match.domain.TeamMatchResult;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamMatchResultCreateResponse {

    private Long matchResultId;
    private Long matchId;

    private int homeScore;
    private String homeTeamName;

    private int awayScore;
    private String awayTeamName;

    private String winnerTeamName;

    public static TeamMatchResultCreateResponse of(TeamMatchResult teamMatchResult) {
        return new TeamMatchResultCreateResponse(
                teamMatchResult.getId(),

                teamMatchResult.getTeamMatch().getId(),
                teamMatchResult.getHomeScore(),
                teamMatchResult.getTeamMatch().getHomeTeam().getTeamName(),

                teamMatchResult.getAwayScore(),
                teamMatchResult.getTeamMatch().getAwayTeam().getTeamName(),

                teamMatchResult.getWinnerTeam() == null ? null : teamMatchResult.getWinnerTeam().getTeamName()
        );

    }
}
