package com.dhoon.footmatch.teammember.dto.response;

import com.dhoon.footmatch.team.domain.Team;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamMembersDto {

    private Long teamId;
    private int teamRating;
    private LocalDateTime createdAt; // 팀 생성일

    private String leaderMemberUsername;
    private int leaderMemberRating;

    private List<TeamMemberDto> teamMembers = new ArrayList<>();

    public static TeamMembersDto of(Team team, List<TeamMemberDto> teamMembers) {
        return new TeamMembersDto(
                team.getId(),
                team.getRating(),
                team.getCreatedAt(),
                team.getLeaderMember().getUsername(),
                team.getLeaderMember().getRating(),
                teamMembers
        );
    }


}
