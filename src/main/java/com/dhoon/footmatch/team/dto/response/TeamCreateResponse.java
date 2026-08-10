package com.dhoon.footmatch.team.dto.response;

import com.dhoon.footmatch.team.domain.Team;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamCreateResponse {

    private Long teamId;
    private String teamName;

    public static TeamCreateResponse of(Team team) {
        return new TeamCreateResponse(team.getId(), team.getTeamName());
    }
}
