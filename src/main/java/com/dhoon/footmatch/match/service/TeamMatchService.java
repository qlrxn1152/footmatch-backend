package com.dhoon.footmatch.match.service;

import com.dhoon.footmatch.match.domain.TeamMatch;
import com.dhoon.footmatch.match.domain.TeamMatchAcceptRequest;
import com.dhoon.footmatch.match.dto.request.TeamMatchCreateRequest;
import com.dhoon.footmatch.match.dto.request.TeamMatchResultCreateRequest;
import com.dhoon.footmatch.match.dto.response.*;

import java.time.LocalDateTime;
import java.util.List;

public interface TeamMatchService {

    TeamMatchCreateResponse createTeamMatch(Long teamId, Long requesterMemberId, TeamMatchCreateRequest request);

    TeamMatchAcceptRequestResponse requestTeamMatchAcceptance(Long matchId, Long requesterMemberId);

    TeamMatchPendingListResponse getPendingMatches();

    TeamMatchAcceptRequestsResponse getTeamMatchAcceptRequests(Long teamId, Long requesterMemberId);

    TeamMatchMatchedResponse acceptTeamMatchRequest(Long matchId, Long requestId, Long requesterMemberId);

    TeamPendingMatchesResponse getTeamPendingMatches(Long teamId);

    TeamMatchedMatchesResponse getTeamMatchedMatches(Long teamId);

    TeamMatchedMatchesResponse getMatchedMatches();

    TeamMatchResultCreateResponse createTeamMatchResult(Long matchId, TeamMatchResultCreateRequest request);

    TeamCompletedMatchesResponse getTeamCompletedMatches(Long teamId);

    TeamCompletedMatchesResponse getCompletedMatches();








}
