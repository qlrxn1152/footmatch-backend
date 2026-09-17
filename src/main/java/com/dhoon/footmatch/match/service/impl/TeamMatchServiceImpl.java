package com.dhoon.footmatch.match.service.impl;


import com.dhoon.footmatch.match.domain.*;
import com.dhoon.footmatch.match.dto.request.TeamMatchCreateRequest;
import com.dhoon.footmatch.match.dto.request.TeamMatchResultCreateRequest;
import com.dhoon.footmatch.match.dto.response.*;
import com.dhoon.footmatch.match.exception.exceptions.*;
import com.dhoon.footmatch.match.repository.TeamMatchAcceptRequestRepository;
import com.dhoon.footmatch.match.repository.TeamMatchRepository;
import com.dhoon.footmatch.match.repository.TeamMatchResultRepository;
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
    private final TeamMatchResultRepository teamMatchResultRepository;

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
        TeamMatchAcceptRequest acceptRequest = teamMatchAcceptRequestRepository.findByIdAndTeamMatchId(requestId, matchId)
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

        completeMatchAcceptance(acceptRequest, teamMatch);

        return TeamMatchMatchedResponse.of(teamMatch);
    }

    @Override
    @Transactional(readOnly = true)
    public TeamPendingMatchesResponse getTeamPendingMatches(Long teamId) {
        List<TeamPendingMatchResponse> pendingMatches = teamMatchRepository.findAllByHomeTeamIdAndStatus(teamId, TeamMatchStatus.PENDING)
                .stream()
                .map(TeamPendingMatchResponse::of)
                .toList();

        return TeamPendingMatchesResponse.of(pendingMatches);
    }

    @Override
    @Transactional(readOnly = true)
    public TeamMatchedMatchesResponse getTeamMatchedMatches(Long teamId) {
        List<TeamMatchedMatchResponse> matchedMatches = teamMatchRepository.findAllTheTeamMatchedMatches(teamId, TeamMatchStatus.MATCHED)
                .stream()
                .map(TeamMatchedMatchResponse::of)
                .toList();

        // 요청한 파라미터에 있는 TeamId 가 , 홈팀으로 속해져있는지 원정팀으로 속해져있는지 확인 ...
        return TeamMatchedMatchesResponse.of(matchedMatches);
    }

    @Override
    @Transactional(readOnly = true)
    public TeamMatchedMatchesResponse getMatchedMatches() {
        List<TeamMatchedMatchResponse> matchedMatches = teamMatchRepository.findAllByTeamMatchStatus(TeamMatchStatus.MATCHED)
                .stream()
                .map(TeamMatchedMatchResponse::of)
                .toList();

        return TeamMatchedMatchesResponse.of(matchedMatches);
    }

    @Override
    public TeamMatchResultCreateResponse createTeamMatchResult(Long matchId, TeamMatchResultCreateRequest request) {
        TeamMatch match = teamMatchValidation.validateTeamMatchExistAndReturn(matchId);

        Team winnerTeam = null;
        int homeScore = request.getHomeScore();
        int awayScore = request.getAwayScore();

        if (homeScore > awayScore) {
            winnerTeam = match.getHomeTeam();
        }

        else if ( awayScore > homeScore) {
            winnerTeam = match.getAwayTeam();
        }

        // 무승부 => winnerTeam = null ( ? )
        TeamMatchResult matchResult = TeamMatchResult.of(match, winnerTeam, homeScore, awayScore);
        match.completed();
        teamMatchResultRepository.save(matchResult);

        return TeamMatchResultCreateResponse.of(matchResult);
    }

    @Override
    @Transactional(readOnly = true)
    public TeamCompletedMatchesResponse getTeamCompletedMatches(Long teamId) {
        List<TeamCompletedMatchResponse> completedMatches = teamMatchResultRepository.findAllTheTeamMatchedMatches(teamId, TeamMatchStatus.COMPLETED)
                .stream()
                .map(TeamCompletedMatchResponse::of)
                .toList();

        return TeamCompletedMatchesResponse.of(completedMatches);
    }

    @Override
    @Transactional(readOnly = true)
    public TeamCompletedMatchesResponse getCompletedMatches() {
        List<TeamCompletedMatchResponse> completedMatches = teamMatchResultRepository.findAllByTeamMatchStatus(TeamMatchStatus.COMPLETED)
                .stream()
                .map(TeamCompletedMatchResponse::of)
                .toList();

        return TeamCompletedMatchesResponse.of(completedMatches);
    }





    private void completeMatchAcceptance(TeamMatchAcceptRequest acceptRequest, TeamMatch teamMatch) {
        acceptRequest.acceptRequest(); // request -> status = ACCEPTED
        teamMatch.match(acceptRequest.getTeam()); // 원정팀 할당, status = MATCHED

        rejectOtherRequests(teamMatch.getId(), acceptRequest.getId());
    }

    private void rejectOtherRequests(Long matchId, Long requestId) {
        teamMatchAcceptRequestRepository.findAllByTeamMatchIdAndStatus(matchId, TeamMatchAcceptRequestStatus.PENDING)
                .stream()
                .filter(request -> !request.getId().equals(requestId))
                .forEach(TeamMatchAcceptRequest::rejectRequest);
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

    private void validateForGetAcceptRequests(Long teamId, Long requesterMemberId) {
        memberValidator.validateExistMember(requesterMemberId);
        Team team = teamValidator.validateExistTeamAndReturn(teamId);
        teamMemberValidator.validateMemberBelongsToTeam(teamId, requesterMemberId);
        teamValidator.validateCheckTeamLeader(team, requesterMemberId);
    }

}
