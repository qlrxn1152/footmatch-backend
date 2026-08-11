package com.dhoon.footmatch.team.dto.response;

import com.dhoon.footmatch.member.domain.Member;
import com.dhoon.footmatch.team.domain.Team;
import com.dhoon.footmatch.teammember.domain.TeamMember;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamDetailResponse {

    private Long teamId;
    private String teamName;
    private Long leaderId;
    private String leaderUsername;
    private int teamRating;
    private long teamMemberCount;

    public static TeamDetailResponse of(Team team, long teamMemberCount) {
        return new TeamDetailResponse(
                team.getId(),
                team.getTeamName(),
                team.getLeaderMember().getId(),
                team.getLeaderMember().getUsername(),
                team.getRating(),
                teamMemberCount
        );
    }

}
