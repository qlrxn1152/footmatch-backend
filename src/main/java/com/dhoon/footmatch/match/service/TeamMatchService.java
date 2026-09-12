package com.dhoon.footmatch.match.service;

import com.dhoon.footmatch.match.domain.TeamMatch;
import com.dhoon.footmatch.match.dto.request.TeamMatchCreateRequest;
import com.dhoon.footmatch.match.dto.response.TeamMatchCreateResponse;

import java.time.LocalDateTime;

public interface TeamMatchService {

    TeamMatchCreateResponse createTeamMatch(Long teamId, Long requesterMemberId, TeamMatchCreateRequest request);





}
