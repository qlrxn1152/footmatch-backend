package com.dhoon.footmatch.match.dto.response;

import com.dhoon.footmatch.match.domain.TeamMatchResult;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamCompletedMatchResponse {

    private Long matchId;

    private Long homeTeamId;
    private String homeTeamName;
    private int homeTeamRating;
    private String homeTeamLeaderUsername;

    private Long awayTeamId;
    private String awayTeamName;
    private int awayTeamRating;
    private String awayTeamLeaderUsername;

    private LocalDateTime matchPlayedAt;

    private String winnerTeamName;

    public static TeamCompletedMatchResponse of(TeamMatchResult teamMatchResult) {
        return new TeamCompletedMatchResponse(
                teamMatchResult.getTeamMatch().getId(),
                teamMatchResult.getTeamMatch().getHomeTeam().getId(),
                teamMatchResult.getTeamMatch().getHomeTeam().getTeamName(),
                teamMatchResult.getTeamMatch().getHomeTeam().getRating(),
                teamMatchResult.getTeamMatch().getHomeTeam().getLeaderMember().getUsername(),

                teamMatchResult.getTeamMatch().getAwayTeam().getId(),
                teamMatchResult.getTeamMatch().getAwayTeam().getTeamName(),
                teamMatchResult.getTeamMatch().getAwayTeam().getRating(),
                teamMatchResult.getTeamMatch().getAwayTeam().getLeaderMember().getUsername(),

                teamMatchResult.getTeamMatch().getPlayedAt(),

                teamMatchResult.getWinnerTeam() == null ? null : teamMatchResult.getWinnerTeam().getTeamName()
        );
    }

}
