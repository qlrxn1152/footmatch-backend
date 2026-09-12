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
public class TeamMatchCreateResponse {

    private Long matchId;
    private Long homeTeamId;
    private String homeTeamName;
    private String homeTeamLeaderUsername;
    private LocalDateTime createdAt;
    private LocalDateTime playedAt;

    public static TeamMatchCreateResponse of(TeamMatch teamMatch) {
        return new TeamMatchCreateResponse(
                teamMatch.getId(),
                teamMatch.getHomeTeam().getId(),
                teamMatch.getHomeTeam().getTeamName(),
                teamMatch.getHomeTeam().getLeaderMember().getUsername(),
                teamMatch.getCreatedAt(),
                teamMatch.getPlayedAt()
        );
    }


}
