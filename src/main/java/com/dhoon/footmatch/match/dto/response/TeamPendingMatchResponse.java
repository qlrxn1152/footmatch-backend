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
public class TeamPendingMatchResponse {

    private Long matchId;
    private Long teamId;
    private String teamName;
    private int teamRating;
    private String teamLeaderUsername;
    private LocalDateTime matchCreatedAt;

    public static TeamPendingMatchResponse of(TeamMatch teamMatch) {
        return new TeamPendingMatchResponse(
                teamMatch.getId(),
                teamMatch.getHomeTeam().getId(),
                teamMatch.getHomeTeam().getTeamName(),
                teamMatch.getHomeTeam().getRating(),
                teamMatch.getHomeTeam().getLeaderMember().getUsername(),
                teamMatch.getCreatedAt()
        );
    }

}
