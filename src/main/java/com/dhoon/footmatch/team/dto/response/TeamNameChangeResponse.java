package com.dhoon.footmatch.team.dto.response;

import com.dhoon.footmatch.team.domain.Team;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamNameChangeResponse {

    private Long teamId;
    private String newTeamName;

    public static TeamNameChangeResponse of(Team team) {
        return new TeamNameChangeResponse(team.getId(), team.getTeamName());
    }


}
