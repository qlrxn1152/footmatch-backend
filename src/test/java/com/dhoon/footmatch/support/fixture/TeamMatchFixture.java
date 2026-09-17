package com.dhoon.footmatch.support.fixture;

import com.dhoon.footmatch.match.dto.request.TeamMatchCreateRequest;
import com.dhoon.footmatch.match.dto.response.TeamMatchAcceptRequestResponse;
import com.dhoon.footmatch.match.dto.response.TeamMatchCreateResponse;
import com.dhoon.footmatch.match.dto.response.TeamMatchMatchedResponse;
import com.dhoon.footmatch.match.service.TeamMatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TeamMatchFixture {

    private final TeamMatchService teamMatchService;

    public TeamMatchCreateResponse createTeamMatch(Long teamId, Long requesterId, LocalDateTime playedAt) {
        return teamMatchService.createTeamMatch(teamId, requesterId, new TeamMatchCreateRequest(playedAt));
    }

    public TeamMatchMatchedResponse createMatchAndMatched(Long homeTeamId, Long homeTeamLeaderId, LocalDateTime playedAt, Long awayTeamLeaderId) {
        TeamMatchCreateResponse matchCreate = createTeamMatch(homeTeamId, homeTeamLeaderId, playedAt);
        TeamMatchAcceptRequestResponse matchRequest = teamMatchService.requestTeamMatchAcceptance(matchCreate.getMatchId(), awayTeamLeaderId);


        return teamMatchService.acceptTeamMatchRequest(matchCreate.getMatchId(), matchRequest.getRequestId(), homeTeamLeaderId);
    }


}
