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
public class TeamMatchedMatchResponse {

    private Long matchId;

    private Long homeTeamId;
    private String homeTeamName;
    private int homeTeamRating;
    private String homeTeamLeaderUsername;

    private Long awayTeamId;
    private String awayTeamName;
    private int awayTeamRating;
    private String awayTeamLeaderUsername;

    // 요청한 파라미터에 있는 TeamId 가 , 홈팀으로 속해져있는지 원정팀으로 속해져있는지 확인 ...

    private LocalDateTime matchCreatedAt;
    private LocalDateTime matchPlayedAt;

    public static TeamMatchedMatchResponse of(TeamMatch teamMatch) {
        return new TeamMatchedMatchResponse(
                teamMatch.getId(),

                teamMatch.getHomeTeam().getId(),
                teamMatch.getHomeTeam().getTeamName(),
                teamMatch.getHomeTeam().getRating(),
                teamMatch.getHomeTeam().getLeaderMember().getUsername(),

                // null 이면 어떻게하지?
                teamMatch.getAwayTeam().getId(),
                teamMatch.getAwayTeam().getTeamName(),
                teamMatch.getAwayTeam().getRating(),
                teamMatch.getAwayTeam().getLeaderMember().getUsername(),

                teamMatch.getCreatedAt(),
                teamMatch.getPlayedAt()
        );
    }
}
