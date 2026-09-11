package com.dhoon.footmatch.teamjoinrequest.dto.response;

import com.dhoon.footmatch.team.domain.Team;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamJoinRequestsListResponse {

    private String teamName;
    private List<TeamJoinRequestListDto> requests = new ArrayList<>();

    public static TeamJoinRequestsListResponse of(Team team, List<TeamJoinRequestListDto> requests) {
        return new TeamJoinRequestsListResponse(
                team.getTeamName(),
                requests
        );
    }

}
