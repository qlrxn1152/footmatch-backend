package com.dhoon.footmatch.teamjoinrequest.service.impl;

import com.dhoon.footmatch.member.domain.Member;
import com.dhoon.footmatch.member.validation.MemberValidator;
import com.dhoon.footmatch.team.domain.Team;
import com.dhoon.footmatch.team.validation.TeamValidator;
import com.dhoon.footmatch.teamjoinrequest.domain.TeamJoinRequest;
import com.dhoon.footmatch.teamjoinrequest.domain.TeamJoinRequestStatus;
import com.dhoon.footmatch.teamjoinrequest.dto.response.*;
import com.dhoon.footmatch.teamjoinrequest.exception.exceptions.*;
import com.dhoon.footmatch.teamjoinrequest.repository.TeamJoinRequestRepository;
import com.dhoon.footmatch.teamjoinrequest.service.TeamJoinRequestService;
import com.dhoon.footmatch.teammember.domain.TeamMember;
import com.dhoon.footmatch.teammember.repository.TeamMemberRepository;
import com.dhoon.footmatch.teammember.validation.TeamMemberValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Override
    public TeamJoinRequestRejectResponse rejectRequest(Long teamId, Long requestId, Long leaderMemberId) {
        RejectRequestData result = validateRejectRequest(teamId, requestId, leaderMemberId);

        TeamJoinRequest joinRequest = result.joinRequest();
        joinRequest.rejectJoinRequest();

        return TeamJoinRequestRejectResponse.of(joinRequest);
    }

    @Override
    public TeamJoinRequestCancelResponse cancelRequest(Long teamId, Long requestId, Long requesterMemberId) {
        TeamJoinRequest joinRequest = validateCancelRequest(teamId, requestId, requesterMemberId);

        joinRequest.cancelJoinRequest();

        return TeamJoinRequestCancelResponse.of(joinRequest);
    }

    @Override
    public TeamJoinRequestsListResponse getPendingRequests(Long teamId, Long requesterMemberId) {
        Team team = validateForGetPendingMatches(teamId, requesterMemberId);

        return getPendingMatchesAndToDtoReturn(teamId, team);
    }



    // ========================================== //
    private JoinRequestData validateTeamJoinRequest(Long teamId, Long memberId) {
        Member member = memberValidator.validateExistMemberAndReturn(memberId);
        Team team = teamValidator.validateExistTeamAndReturn(teamId);
        teamMemberValidator.validateMemberNotJoinedTeam(memberId);
        return new JoinRequestData(member, team);
    }

    private void validateNoPendingJoinRequest(Long teamId, Long memberId) {
        if (teamJoinRequestRepository.existsByTeamIdAndMemberIdAndStatus(teamId, memberId, TeamJoinRequestStatus.PENDING)) {
            throw new DuplicateTeamJoinRequestException();
        }
    }

    private void acceptTeamJoinRequest(AcceptRequestData result) {
        teamMemberRepository.save(TeamMember.createMember(result.team(), result.joinRequest().getMember())); // 팀원으로 ..
        result.joinRequest().acceptJoinRequest(); // 해당 가입신청 ACCEPTED

        teamJoinRequestRepository.findAllByMemberIdAndStatus(result.joinRequest().getMember().getId(), TeamJoinRequestStatus.PENDING)
                .forEach(TeamJoinRequest::cancelJoinRequest); // 해당 가입요청을 제외한 나머지 해당유저의 가입신청들은 CANCELED 로 변경..
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

    private RejectRequestData validateRejectRequest(Long teamId, Long requestId, Long leaderMemberId) {
        TeamJoinRequest joinRequest = teamJoinRequestRepository.findById(requestId)
                .orElseThrow(NotFoundTeamJoinRequestException::new);

        if (!joinRequest.getTeam().getId().equals(teamId)) {
            throw new NotTeamJoinRequestException();
        }

        if (joinRequest.getStatus() != TeamJoinRequestStatus.PENDING) {
            throw new TeamJoinRequestStatusException();
        }

        memberValidator.validateExistMemberAndReturn(leaderMemberId);
        Team team = teamValidator.validateExistTeamAndReturn(teamId);

        teamMemberValidator.validateMemberBelongsToTeam(teamId, leaderMemberId);
        teamValidator.validateCheckTeamLeader(team, leaderMemberId);

        memberValidator.validateExistMember(joinRequest.getMember().getId());
        return new RejectRequestData(joinRequest, team);
    }

    private TeamJoinRequest validateCancelRequest(Long teamId, Long requestId, Long requesterMemberId) {
        TeamJoinRequest joinRequest = teamJoinRequestRepository.findById(requestId)
                .orElseThrow(NotFoundTeamJoinRequestException::new);

        if (!joinRequest.getTeam().getId().equals(teamId)) {
            throw new NotTeamJoinRequestException();
        }

        if ( joinRequest.getStatus() != TeamJoinRequestStatus.PENDING) {
            throw new TeamJoinRequestStatusException();
        }

        memberValidator.validateExistMember(requesterMemberId);

        if (!joinRequest.getMember().getId().equals(requesterMemberId)) {
            throw new NotTeamJoinRequestOwnerException();
        }

        return joinRequest;
    }


    private TeamJoinRequestsListResponse getPendingMatchesAndToDtoReturn(Long teamId, Team team) {
        List<TeamJoinRequestListDto> requests = teamJoinRequestRepository.findAllByTeamIdAndStatus(teamId, TeamJoinRequestStatus.PENDING)
                .stream()
                .map(TeamJoinRequestListDto::of)
                .toList();


        return TeamJoinRequestsListResponse.of(team, requests);
    }

    private @NonNull Team validateForGetPendingMatches(Long teamId, Long requesterMemberId) {
        Team team = teamValidator.validateExistTeamAndReturn(teamId);
        memberValidator.validateExistMemberAndReturn(requesterMemberId);
        teamMemberValidator.validateMemberBelongsToTeam(teamId, requesterMemberId);
        teamValidator.validateCheckTeamLeader(team, requesterMemberId);
        return team;
    }


    private record AcceptRequestData(TeamJoinRequest joinRequest, Team team) { }

    private record RejectRequestData(TeamJoinRequest joinRequest, Team team) { }

    private record JoinRequestData(Member member, Team team) {}
}
