package com.dhoon.footmatch.teamjoinrequest.dto.response;

import com.dhoon.footmatch.teamjoinrequest.domain.TeamJoinRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamJoinRequestAcceptResponse {

    private Long requestId;

    private Long memberId;
    private String memberUsername;

    private Long teamId;
    private String teamName;

    public static TeamJoinRequestAcceptResponse of(TeamJoinRequest teamJoinRequest) {
        return new TeamJoinRequestAcceptResponse(
                teamJoinRequest.getId(),
                teamJoinRequest.getMember().getId(),
                teamJoinRequest.getMember().getUsername(),
                teamJoinRequest.getTeam().getId(),
                teamJoinRequest.getTeam().getTeamName()
        );
    }
}
