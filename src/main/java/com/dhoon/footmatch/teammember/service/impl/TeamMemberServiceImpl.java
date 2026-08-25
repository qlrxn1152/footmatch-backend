package com.dhoon.footmatch.teammember.service.impl;

import com.dhoon.footmatch.member.validation.MemberValidator;
import com.dhoon.footmatch.team.domain.Team;
import com.dhoon.footmatch.team.validation.TeamValidator;
import com.dhoon.footmatch.teammember.domain.TeamMember;
import com.dhoon.footmatch.teammember.dto.response.TeamLeaveResponse;
import com.dhoon.footmatch.teammember.exception.exceptions.TeamLeaderCannotLeaveException;
import com.dhoon.footmatch.teammember.repository.TeamMemberRepository;
import com.dhoon.footmatch.teammember.service.TeamMemberService;
import com.dhoon.footmatch.teammember.validation.TeamMemberValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Slf4j
@Service
public class TeamMemberServiceImpl implements TeamMemberService {

    private final TeamMemberRepository teamMemberRepository;

    private final TeamMemberValidator teamMemberValidator;
    private final MemberValidator memberValidator;
    private final TeamValidator teamValidator;

    @Override
    public TeamLeaveResponse leaveTeam(Long teamId, Long memberId) {
        TeamMember teamMember = validateLeaveTeamAndGetMember(teamId, memberId);

        teamMemberRepository.delete(teamMember);

        return TeamLeaveResponse.of(teamMember);
    }




    // ============================================== //
    private TeamMember validateLeaveTeamAndGetMember(Long teamId, Long memberId) {
        memberValidator.validateExistMember(memberId);
        Team team = teamValidator.validateExistTeamAndReturn(teamId);

        TeamMember teamMember = teamMemberValidator.validateMemberBelongsToTeamAndReturn(memberId);
        teamMemberValidator.validateMemberBelongsToTeam(teamId, memberId);

        if ( team.getLeaderMember().getId().equals(memberId) ) {
            throw new TeamLeaderCannotLeaveException();
        }
        return teamMember;
    }


}
