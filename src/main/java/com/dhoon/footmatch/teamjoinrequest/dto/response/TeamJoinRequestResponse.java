package com.dhoon.footmatch.teamjoinrequest.dto.response;

import com.dhoon.footmatch.teamjoinrequest.domain.TeamJoinRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamJoinRequestResponse {

    private Long joinRequestId;
    private Long teamId;
    private String teamName;
    private Long memberId;

    public static TeamJoinRequestResponse of(TeamJoinRequest joinRequest) {
        return new TeamJoinRequestResponse(
                joinRequest.getId(),
                joinRequest.getTeam().getId(),
                joinRequest.getTeam().getTeamName(),
                joinRequest.getMember().getId()
        );
    }

}
