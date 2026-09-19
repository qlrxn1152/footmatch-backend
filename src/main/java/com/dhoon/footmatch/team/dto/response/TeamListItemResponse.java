package com.dhoon.footmatch.team.dto.response;

import com.dhoon.footmatch.team.domain.Team;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamListItemResponse {

    private String teamName;
    private int teamRating;
    private String leaderUsername;
    private long memberCount;

    public static TeamListItemResponse of(Team team, long memberCount) {
        return new TeamListItemResponse(
                team.getTeamName(),
                team.getRating(),
                team.getLeaderMember().getUsername(),
                memberCount
        );
    }

}
