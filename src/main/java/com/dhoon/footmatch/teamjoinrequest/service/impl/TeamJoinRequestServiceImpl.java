package com.dhoon.footmatch.teamjoinrequest.service.impl;

import com.dhoon.footmatch.member.domain.Member;
import com.dhoon.footmatch.member.validation.MemberValidator;
import com.dhoon.footmatch.team.domain.Team;
import com.dhoon.footmatch.team.validation.TeamValidator;
import com.dhoon.footmatch.teamjoinrequest.domain.TeamJoinRequest;
import com.dhoon.footmatch.teamjoinrequest.domain.TeamJoinRequestStatus;
import com.dhoon.footmatch.teamjoinrequest.dto.response.TeamJoinRequestAcceptResponse;
import com.dhoon.footmatch.teamjoinrequest.dto.response.TeamJoinRequestResponse;
import com.dhoon.footmatch.teamjoinrequest.exception.exceptions.DuplicateTeamJoinRequestException;
import com.dhoon.footmatch.teamjoinrequest.exception.exceptions.NotFoundTeamJoinRequestException;
import com.dhoon.footmatch.teamjoinrequest.exception.exceptions.NotTeamJoinRequestException;
import com.dhoon.footmatch.teamjoinrequest.exception.exceptions.TeamJoinRequestStatusException;
import com.dhoon.footmatch.teamjoinrequest.repository.TeamJoinRequestRepository;
import com.dhoon.footmatch.teamjoinrequest.service.TeamJoinRequestService;
import com.dhoon.footmatch.teammember.domain.TeamMember;
import com.dhoon.footmatch.teammember.repository.TeamMemberRepository;
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
    private final TeamMemberRepository teamMemberRepository;

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


    @Override
    public TeamJoinRequestAcceptResponse acceptRequest(Long teamId, Long requestId, Long leaderMemberId) {
        AcceptRequestData result = validateAcceptRequest(teamId, requestId, leaderMemberId);
        acceptTeamJoinRequest(result);

        return TeamJoinRequestAcceptResponse.of(result.joinRequest());
    }



    // ========================================== //
    private JoinRequestData validateTeamJoinRequest(Long teamId, Long memberId) {
        Member member = memberValidator.validateExistMemberAndReturn(memberId);
        Team team = teamValidator.validateExistTeamAndReturn(teamId);
        teamMemberValidator.validateMemberNotJoinedTeam(memberId);
        return new JoinRequestData(member, team);
    }

    private record JoinRequestData(Member member, Team team) {}

    private void validateNoPendingJoinRequest(Long teamId, Long memberId) {
        if (teamJoinRequestRepository.existsByTeamIdAndMemberIdAndStatus(teamId, memberId, TeamJoinRequestStatus.PENDING)) {
            throw new DuplicateTeamJoinRequestException();
        }
    }

    private void acceptTeamJoinRequest(AcceptRequestData result) {
        teamMemberRepository.save(TeamMember.createMember(result.team(), result.joinRequest().getMember()));
        result.joinRequest().acceptJoinRequest();

        teamJoinRequestRepository.findAllByMemberIdAndStatus(result.joinRequest().getMember().getId(), TeamJoinRequestStatus.PENDING)
                .forEach(TeamJoinRequest::cancelJoinRequest);
    }



    private AcceptRequestData validateAcceptRequest(Long teamId, Long requestId, Long leaderMemberId) {
        TeamJoinRequest joinRequest = teamJoinRequestRepository.findById(requestId)
                .orElseThrow(NotFoundTeamJoinRequestException::new);

        if (!joinRequest.getTeam().getId().equals(teamId)) {
            throw new NotTeamJoinRequestException();
        }

        if (joinRequest.getStatus() != TeamJoinRequestStatus.PENDING) {
            throw new TeamJoinRequestStatusException();
        }

        teamMemberValidator.validateMemberNotJoinedTeam(joinRequest.getMember().getId());

        memberValidator.validateExistMemberAndReturn(leaderMemberId);
        Team team = teamValidator.validateExistTeamAndReturn(teamId);

        teamMemberValidator.validateMemberBelongsToTeam(teamId, leaderMemberId);
        teamValidator.validateCheckTeamLeader(team, leaderMemberId);

        memberValidator.validateExistMember(joinRequest.getMember().getId());
        return new AcceptRequestData(joinRequest, team);
    }

    private record AcceptRequestData(TeamJoinRequest joinRequest, Team team) {
    }

}
