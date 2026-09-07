package com.dhoon.footmatch.teammember.service;

import com.dhoon.footmatch.teammember.domain.TeamMember;
import com.dhoon.footmatch.teammember.dto.response.TeamLeaveResponse;
import com.dhoon.footmatch.teammember.dto.response.TeamMembersDto;

import java.util.List;

public interface TeamMemberService {

    TeamLeaveResponse leaveTeam(Long teamId, Long memberId);

    void kickMember(Long teamId, Long requesterId, Long targeterId);

    TeamMembersDto getTeamMembers(Long teamId);


}
