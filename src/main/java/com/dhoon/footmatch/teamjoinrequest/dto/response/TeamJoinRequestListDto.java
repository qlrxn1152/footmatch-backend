package com.dhoon.footmatch.teamjoinrequest.dto.response;

import com.dhoon.footmatch.teamjoinrequest.domain.TeamJoinRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamJoinRequestListDto {

    private String username;
    private int userRating;
    private LocalDateTime createdAt;

    public static TeamJoinRequestListDto of(TeamJoinRequest teamJoinRequest) {
        return new TeamJoinRequestListDto(
                teamJoinRequest.getMember().getUsername(),
                teamJoinRequest.getMember().getRating(),
                teamJoinRequest.getCreatedAt()
        );
    }

}
