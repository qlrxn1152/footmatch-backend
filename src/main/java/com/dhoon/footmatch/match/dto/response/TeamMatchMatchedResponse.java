package com.dhoon.footmatch.match.dto.response;

import com.dhoon.footmatch.match.domain.TeamMatch;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamMatchMatchedResponse {

    private Long matchId;

    private String homeTeamName;
    private int homeTeamRating;
    private String homeTeamLeaderUsername;

    private String awayTeamName;
    private int awayTeamRating;
    private String awayTeamLeaderUsername;

    private LocalDateTime matchedAt;

    public static TeamMatchMatchedResponse of(TeamMatch teamMatch) {
        return new TeamMatchMatchedResponse(
                teamMatch.getId(),

                teamMatch.getHomeTeam().getTeamName(),
                teamMatch.getHomeTeam().getRating(),
                teamMatch.getHomeTeam().getLeaderMember().getUsername(),

                teamMatch.getAwayTeam().getTeamName(),
                teamMatch.getAwayTeam().getRating(),
                teamMatch.getAwayTeam().getLeaderMember().getUsername(),

                LocalDateTime.now()
        );

    }
}
