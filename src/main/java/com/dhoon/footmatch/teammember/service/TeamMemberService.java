package com.dhoon.footmatch.teammember.service;

import com.dhoon.footmatch.teammember.dto.response.TeamLeaveResponse;

public interface TeamMemberService {

    TeamLeaveResponse leaveTeam(Long teamId, Long memberId);
}
