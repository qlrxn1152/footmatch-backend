package com.dhoon.footmatch.teammember.dto.response;

import com.dhoon.footmatch.teammember.domain.TeamMember;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamLeaveResponse {

    private Long teamId;
    private String teamName;

    private Long memberId;
    private String username;

    public static TeamLeaveResponse of(TeamMember teamMember) {
        return new TeamLeaveResponse(
                teamMember.getTeam().getId(),
                teamMember.getTeam().getTeamName(),
                teamMember.getMember().getId(),
                teamMember.getMember().getUsername()
        );
    }
}
