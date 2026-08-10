package com.dhoon.footmatch.team.service;

import com.dhoon.footmatch.team.dto.request.TeamCreateRequest;
import com.dhoon.footmatch.team.dto.response.TeamCreateResponse;

public interface TeamService {

    TeamCreateResponse createTeam(TeamCreateRequest request, Long memberId);
}
