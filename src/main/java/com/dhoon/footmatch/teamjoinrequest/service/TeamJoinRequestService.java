package com.dhoon.footmatch.teamjoinrequest.service;

import com.dhoon.footmatch.teamjoinrequest.dto.response.*;

public interface TeamJoinRequestService {

    TeamJoinRequestResponse joinRequest(Long teamId, Long memberId);

    TeamJoinRequestAcceptResponse acceptRequest(Long teamId, Long requestId, Long leaderMemberId);

    TeamJoinRequestRejectResponse rejectRequest(Long teamId, Long requestId, Long leaderMemberId);

    TeamJoinRequestCancelResponse cancelRequest(Long teamId, Long requestId, Long requesterMemberId);

    TeamJoinRequestsListResponse getPendingRequests(Long teamId, Long requesterMemberId);



}
