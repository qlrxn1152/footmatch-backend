package com.dhoon.footmatch.match.service;

import com.dhoon.footmatch.match.domain.TeamMatch;
import com.dhoon.footmatch.match.domain.TeamMatchAcceptRequest;
import com.dhoon.footmatch.match.dto.request.TeamMatchCreateRequest;
import com.dhoon.footmatch.match.dto.response.*;

import java.time.LocalDateTime;

public interface TeamMatchService {

    TeamMatchCreateResponse createTeamMatch(Long teamId, Long requesterMemberId, TeamMatchCreateRequest request);

    TeamMatchAcceptRequestResponse requestTeamMatchAcceptance(Long matchId, Long requesterMemberId);

    TeamMatchPendingListResponse getPendingMatches();

    TeamMatchAcceptRequestsResponse getTeamMatchAcceptRequests(Long teamId, Long requesterMemberId);

    TeamMatchMatchedResponse acceptTeamMatchRequest(Long matchId, Long requestId, Long requesterMemberId);





}
