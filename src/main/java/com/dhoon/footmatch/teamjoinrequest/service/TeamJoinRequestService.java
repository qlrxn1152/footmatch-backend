package com.dhoon.footmatch.teamjoinrequest.service;

import com.dhoon.footmatch.teamjoinrequest.dto.response.TeamJoinRequestResponse;

public interface TeamJoinRequestService {

    TeamJoinRequestResponse joinRequest(Long teamId, Long memberId);
}
