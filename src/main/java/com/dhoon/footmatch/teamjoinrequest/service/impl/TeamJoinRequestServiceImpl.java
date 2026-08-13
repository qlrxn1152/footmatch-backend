package com.dhoon.footmatch.teamjoinrequest.service.impl;

import com.dhoon.footmatch.member.domain.Member;
import com.dhoon.footmatch.member.validation.MemberValidator;
import com.dhoon.footmatch.team.domain.Team;
import com.dhoon.footmatch.team.validation.TeamValidator;
import com.dhoon.footmatch.teamjoinrequest.domain.TeamJoinRequest;
import com.dhoon.footmatch.teamjoinrequest.domain.TeamJoinRequestStatus;
import com.dhoon.footmatch.teamjoinrequest.dto.response.TeamJoinRequestResponse;
import com.dhoon.footmatch.teamjoinrequest.exception.exceptions.DuplicateTeamJoinRequestException;
import com.dhoon.footmatch.teamjoinrequest.repository.TeamJoinRequestRepository;
import com.dhoon.footmatch.teamjoinrequest.service.TeamJoinRequestService;
import com.dhoon.footmatch.teammember.validation.TeamMemberValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class TeamJoinRequestServiceImpl implements TeamJoinRequestService {

    private final TeamJoinRequestRepository teamJoinRequestRepository;

    private final MemberValidator memberValidator;
    private final TeamValidator teamValidator;
    private final TeamMemberValidator teamMemberValidator;

    @Override
    public TeamJoinRequestResponse joinRequest(Long teamId, Long memberId) {

        JoinRequestData joinRequestData = validateTeamJoinRequest(teamId, memberId);

        validateNoPendingJoinRequest(teamId, memberId);

        TeamJoinRequest joinRequest = teamJoinRequestRepository.save(TeamJoinRequest.createJoinRequest(joinRequestData.team(), joinRequestData.member()));

        return TeamJoinRequestResponse.of(joinRequest);
    }

    private JoinRequestData validateTeamJoinRequest(Long teamId, Long memberId) {
        Member member = memberValidator.validateExistMemberAndReturn(memberId);
        Team team = teamValidator.validateExistTeamAndReturn(teamId);
        teamMemberValidator.validateMemberNotJoinedTeam(memberId);
        return new JoinRequestData(member, team);
    }

    private record JoinRequestData(Member member, Team team) {
    }

    private void validateNoPendingJoinRequest(Long teamId, Long memberId) {
        if (teamJoinRequestRepository.existsByTeamIdAndMemberIdAndStatus(teamId, memberId, TeamJoinRequestStatus.PENDING)) {
            throw new DuplicateTeamJoinRequestException();
        }
    }

}
