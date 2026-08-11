package com.dhoon.footmatch.team.dto.response;

import com.dhoon.footmatch.member.domain.Member;
import com.dhoon.footmatch.team.domain.Team;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamLeaderTransferResponse {

    private Long teamId;
    private String teamName;

    private Long oldLeaderMemberId;
    private String oldLeaderName;

    private Long newLeaderMemberId;
    private String newLeaderName;

    public static TeamLeaderTransferResponse of(Team team, Member oldMember, Member newMember) {
        return new TeamLeaderTransferResponse(
                team.getId(),
                team.getTeamName(),
                oldMember.getId(),
                oldMember.getUsername(),
                newMember.getId(),
                newMember.getUsername()
        );
    }
}
