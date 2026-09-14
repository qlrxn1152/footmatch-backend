package com.dhoon.footmatch.match.service.impl;


import com.dhoon.footmatch.match.domain.TeamMatch;
import com.dhoon.footmatch.match.domain.TeamMatchAcceptRequest;
import com.dhoon.footmatch.match.domain.TeamMatchAcceptRequestStatus;
import com.dhoon.footmatch.match.domain.TeamMatchStatus;
import com.dhoon.footmatch.match.dto.request.TeamMatchCreateRequest;
import com.dhoon.footmatch.match.dto.response.*;
import com.dhoon.footmatch.match.exception.exceptions.*;
import com.dhoon.footmatch.match.repository.TeamMatchAcceptRequestRepository;
import com.dhoon.footmatch.match.repository.TeamMatchRepository;
import com.dhoon.footmatch.match.service.TeamMatchService;
import com.dhoon.footmatch.match.validation.TeamMatchValidation;
import com.dhoon.footmatch.member.domain.Member;
import com.dhoon.footmatch.member.validation.MemberValidator;
import com.dhoon.footmatch.team.domain.Team;
import com.dhoon.footmatch.team.validation.TeamValidator;
import com.dhoon.footmatch.teammember.domain.TeamMember;
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
public class TeamMatchServiceImpl implements TeamMatchService {

    private final TeamMatchRepository teamMatchRepository;
    private final TeamMatchAcceptRequestRepository teamMatchAcceptRequestRepository;

    private final TeamValidator teamValidator;
    private final MemberValidator memberValidator;
    private final TeamMemberValidator teamMemberValidator;
    private final TeamMatchValidation teamMatchValidation;

    @Override
    public TeamMatchCreateResponse createTeamMatch(Long teamId, Long requesterMemberId, TeamMatchCreateRequest request) {
        Team team = validateForCreateTeamMatch(teamId, requesterMemberId, request);
        TeamMatch teamMatch = createTeamMatchAndSave(request, team);

        return TeamMatchCreateResponse.of(teamMatch);
    }

    @Override
    public TeamMatchAcceptRequestResponse requestTeamMatchAcceptance(Long matchId, Long requesterMemberId) {
        TeamMatch teamMatch = teamMatchValidation.validateTeamMatchExistAndReturn(matchId);
        teamMatchValidation.validateTeamMatchPendingStatus(teamMatch);
        Member member = memberValidator.validateExistMemberAndReturn(requesterMemberId);
        teamValidator.validateExistTeamAndReturn(teamMatch.getHomeTeam().getId());
        TeamMember teamMember = teamMemberValidator.validateMemberBelongsToTeamAndReturn(requesterMemberId);
        teamValidator.validateExistTeamAndReturn(teamMember.getTeam().getId());
        teamValidator.validateCheckTeamLeader(teamMember.getTeam(), requesterMemberId);

        // 자기팀 매치에 요청을 보내는 경우 검증
        if(teamMatch.getHomeTeam().getId().equals(teamMember.getTeam().getId())) {
            throw new CannotRequestOwnTeamMatchException();
        }
        // 이미 요청을 보낸 매치인지 검증
        if (teamMatchAcceptRequestRepository.existsByTeamMatchIdAndTeamIdAndStatus(matchId, teamMember.getTeam().getId(), TeamMatchAcceptRequestStatus.PENDING)) {
            throw new AlreadyRequestedTeamMatchException();
        }


        TeamMatchAcceptRequest teamMatchAcceptRequest = TeamMatchAcceptRequest.of(teamMatch, member, teamMember.getTeam());

        teamMatchAcceptRequestRepository.save(teamMatchAcceptRequest);
        return TeamMatchAcceptRequestResponse.of(teamMatch, teamMatchAcceptRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public TeamMatchPendingListResponse getPendingMatches() {
        List<TeamMatchPendingResponse> pendingMatches = teamMatchRepository.findAllByTeamMatchStatus(TeamMatchStatus.PENDING)
                .stream()
                .map(TeamMatchPendingResponse::of)
                .toList();

        return TeamMatchPendingListResponse.of(pendingMatches);
    }

    @Override
    @Transactional(readOnly = true)
    public TeamMatchAcceptRequestsResponse getTeamMatchAcceptRequests(Long teamId, Long requesterMemberId) {
        validateForGetAcceptRequests(teamId, requesterMemberId);

        List<TeamMatchAcceptRequestListItemResponse> requests = teamMatchAcceptRequestRepository.findAllByTeamId(teamId)
                .stream()
                .map(TeamMatchAcceptRequestListItemResponse::of)
                .toList();

        return TeamMatchAcceptRequestsResponse.of(requests);
    }

    @Override
    public TeamMatchMatchedResponse acceptTeamMatchRequest(Long matchId, Long requestId, Long requesterMemberId) {
        TeamMatch teamMatch = teamMatchValidation.validateTeamMatchExistAndReturn(matchId);
        TeamMatchAcceptRequest acceptRequest = teamMatchAcceptRequestRepository.findById(requestId)
                .orElseThrow(NotFoundTeamMatchAcceptRequestException::new);

        Team homeTeam = teamValidator.validateExistTeamAndReturn(teamMatch.getHomeTeam().getId());// 홈팀 존재
        teamValidator.validateExistTeamAndReturn(acceptRequest.getTeam().getId()); // 원정팀 존재

        memberValidator.validateExistMember(requesterMemberId);
        teamMemberValidator.validateMemberBelongsToTeam(homeTeam.getId(), requesterMemberId);
        teamValidator.validateCheckTeamLeader(homeTeam, requesterMemberId);
        teamMatchValidation.validateTeamMatchPendingStatus(teamMatch);

        if ( acceptRequest.getStatus() != TeamMatchAcceptRequestStatus.PENDING) {
            throw new InvalidTeamMatchAcceptRequestStatusException();
        }


        matchedMatch(acceptRequest, teamMatch); // 양방향 매핑
        return TeamMatchMatchedResponse.of(teamMatch);
    }

    private static void matchedMatch(TeamMatchAcceptRequest acceptRequest, TeamMatch teamMatch) {
        acceptRequest.acceptRequest(); // request -> status = ACCEPTED
        teamMatch.match(acceptRequest.getTeam()); // 원정팀 할당, status = MATCHED
    }


    //
    private void validateForGetAcceptRequests(Long teamId, Long requesterMemberId) {
        memberValidator.validateExistMember(requesterMemberId);
        Team team = teamValidator.validateExistTeamAndReturn(teamId);
        teamMemberValidator.validateMemberBelongsToTeam(teamId, requesterMemberId);
        teamValidator.validateCheckTeamLeader(team, requesterMemberId);
    }


    private @NonNull TeamMatch createTeamMatchAndSave(TeamMatchCreateRequest request, Team team) {
        TeamMatch teamMatch = TeamMatch.createTeamMatch(team, request.getPlayedAt());
        teamMatchRepository.save(teamMatch);
        return teamMatch;
    }


    private @NonNull Team validateForCreateTeamMatch(Long teamId, Long requesterMemberId, TeamMatchCreateRequest request) {
        Team team = teamValidator.validateExistTeamAndReturn(teamId);
        memberValidator.validateExistMemberAndReturn(requesterMemberId);
        teamMemberValidator.validateMemberBelongsToTeam(teamId, requesterMemberId);
        teamValidator.validateCheckTeamLeader(team, requesterMemberId);
        teamMatchValidation.validatePlayedAt(request);
        teamMatchValidation.validateAlreadyExistPendingMatch(teamId);
        return team;
    }


}
