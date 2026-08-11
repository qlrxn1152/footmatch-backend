package com.dhoon.footmatch.team.service;

import com.dhoon.footmatch.team.dto.request.TeamCreateRequest;
import com.dhoon.footmatch.team.dto.request.TeamNameChangeRequest;
import com.dhoon.footmatch.team.dto.response.TeamCreateResponse;
import com.dhoon.footmatch.team.dto.response.TeamDetailResponse;
import com.dhoon.footmatch.team.dto.response.TeamNameChangeResponse;

public interface TeamService {

    TeamCreateResponse createTeam(TeamCreateRequest request, Long memberId);

    TeamNameChangeResponse changeTeamName(TeamNameChangeRequest request, Long memberId, Long teamId);

    TeamDetailResponse getTeam(Long teamId);
}
