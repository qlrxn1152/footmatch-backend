package com.dhoon.footmatch.teammember.dto.response;

import com.dhoon.footmatch.teammember.domain.TeamMember;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamMemberDto {

    private String username;
    private int memberRating;
    private LocalDateTime joinedAt;

    public static TeamMemberDto of(TeamMember teamMember) {
        return new TeamMemberDto(
                teamMember.getMember().getUsername(),
                teamMember.getMember().getRating(),
                teamMember.getJoinedAt()
        );
    }

}
