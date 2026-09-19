package com.dhoon.footmatch.team.service;

import com.dhoon.footmatch.team.dto.request.TeamCreateRequest;
import com.dhoon.footmatch.team.dto.request.TeamLeaderTransferRequest;
import com.dhoon.footmatch.team.dto.request.TeamNameChangeRequest;
import com.dhoon.footmatch.team.dto.response.*;

public interface TeamService {

    TeamCreateResponse createTeam(TeamCreateRequest request, Long memberId);

    TeamNameChangeResponse changeTeamName(TeamNameChangeRequest request, Long memberId, Long teamId);

    TeamDetailResponse getTeam(Long teamId);

    TeamLeaderTransferResponse transferLeader(Long teamId, Long currentLeaderMemberId, TeamLeaderTransferRequest request);

    TeamListResponse getTeamList();


}
