package com.dhoon.footmatch.teamjoinrequest.dto.response;

import com.dhoon.footmatch.teamjoinrequest.domain.TeamJoinRequest;
import com.dhoon.footmatch.teamjoinrequest.domain.TeamJoinRequestStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamJoinRequestRejectResponse {

    private Long requestId;

    private Long teamId;
    private String teamName;

    private Long memberId;
    private String username;

    private TeamJoinRequestStatus status;

    public static TeamJoinRequestRejectResponse of(TeamJoinRequest teamJoinRequest) {
        return new TeamJoinRequestRejectResponse(
                teamJoinRequest.getId(),
                teamJoinRequest.getTeam().getId(),
                teamJoinRequest.getTeam().getTeamName(),
                teamJoinRequest.getMember().getId(),
                teamJoinRequest.getMember().getUsername(),
                teamJoinRequest.getStatus()
        );
    }
}
