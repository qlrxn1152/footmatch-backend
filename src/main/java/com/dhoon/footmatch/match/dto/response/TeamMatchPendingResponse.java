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
public class TeamMatchPendingResponse {

    private Long matchId;
    private String homeTeamName;
    private int homeTeamRating;
    private String homeTeamLeaderUsername;

    private LocalDateTime matchCreatedAt;
    private LocalDateTime matchPlayedAt;

    public static TeamMatchPendingResponse of(TeamMatch teamMatch) {
        return new TeamMatchPendingResponse(
                teamMatch.getId(),
                teamMatch.getHomeTeam().getTeamName(),
                teamMatch.getHomeTeam().getRating(),
                teamMatch.getHomeTeam().getLeaderMember().getUsername(),
                teamMatch.getCreatedAt(),
                teamMatch.getPlayedAt()
        );
    }

}
