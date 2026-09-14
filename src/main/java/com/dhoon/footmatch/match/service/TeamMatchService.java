package com.dhoon.footmatch.match.service;

import com.dhoon.footmatch.match.domain.TeamMatch;
import com.dhoon.footmatch.match.domain.TeamMatchAcceptRequest;
import com.dhoon.footmatch.match.dto.request.TeamMatchCreateRequest;
import com.dhoon.footmatch.match.dto.response.TeamMatchAcceptRequestResponse;
import com.dhoon.footmatch.match.dto.response.TeamMatchCreateResponse;
import com.dhoon.footmatch.match.dto.response.TeamMatchPendingListResponse;

import java.time.LocalDateTime;

public interface TeamMatchService {

    TeamMatchCreateResponse createTeamMatch(Long teamId, Long requesterMemberId, TeamMatchCreateRequest request);

    TeamMatchAcceptRequestResponse acceptRequestTeamMatch(Long matchId, Long requesterMemberId);

    TeamMatchPendingListResponse getPendingMatches();





}
