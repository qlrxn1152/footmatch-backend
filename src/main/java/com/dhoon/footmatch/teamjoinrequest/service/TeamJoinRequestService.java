package com.dhoon.footmatch.teamjoinrequest.service;

import com.dhoon.footmatch.teamjoinrequest.dto.response.TeamJoinRequestAcceptResponse;
import com.dhoon.footmatch.teamjoinrequest.dto.response.TeamJoinRequestRejectResponse;
import com.dhoon.footmatch.teamjoinrequest.dto.response.TeamJoinRequestResponse;

public interface TeamJoinRequestService {

    TeamJoinRequestResponse joinRequest(Long teamId, Long memberId);

    TeamJoinRequestAcceptResponse acceptRequest(Long teamId, Long requestId, Long leaderMemberId);

    TeamJoinRequestRejectResponse rejectRequest(Long teamId, Long requestId, Long leaderMemberId);
}
