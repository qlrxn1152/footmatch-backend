package com.dhoon.footmatch.match.dto.response;

import com.dhoon.footmatch.match.domain.TeamMatchAcceptRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamMatchAcceptRequestListItemResponse {

    private Long requestId;
    private String requesterUsername;
    private String requesterTeamName;
    private int requesterTeamRating;
    private LocalDateTime requestAt;

    public static TeamMatchAcceptRequestListItemResponse of(TeamMatchAcceptRequest request) {
        return new TeamMatchAcceptRequestListItemResponse(
                request.getId(),
                request.getMember().getUsername(), // 팀장뿐만아니라, 나중에 STAFF .. 가능하게 ..
                request.getTeam().getTeamName(),
                request.getTeam().getRating(),
                request.getCreatedAt()
        );
    }
}
