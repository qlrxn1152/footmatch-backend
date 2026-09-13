package com.dhoon.footmatch.match.service.impl;


import com.dhoon.footmatch.match.domain.TeamMatch;
import com.dhoon.footmatch.match.domain.TeamMatchAcceptRequest;
import com.dhoon.footmatch.match.domain.TeamMatchStatus;
import com.dhoon.footmatch.match.dto.request.TeamMatchCreateRequest;
import com.dhoon.footmatch.match.dto.response.TeamMatchAcceptRequestResponse;
import com.dhoon.footmatch.match.dto.response.TeamMatchCreateResponse;
import com.dhoon.footmatch.match.exception.exceptions.AlreadyExistPendingMatchException;
import com.dhoon.footmatch.match.exception.exceptions.InvalidMatchPlayedAtException;
import com.dhoon.footmatch.match.exception.exceptions.NotFoundTeamMatchException;
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

import java.time.LocalDateTime;

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
    public TeamMatchAcceptRequestResponse acceptRequestTeamMatch(Long matchId, Long requesterMemberId) {
        TeamMatch teamMatch = teamMatchValidation.validateTeamMatchExistAndReturn(matchId);
        teamMatchValidation.validateTeamMatchPendingStatus(teamMatch);
        Member member = memberValidator.validateExistMemberAndReturn(requesterMemberId);
        teamValidator.validateExistTeamAndReturn(teamMatch.getHomeTeam().getId());
        TeamMember teamMember = teamMemberValidator.validateMemberBelongsToTeamAndReturn(requesterMemberId);
        teamValidator.validateExistTeamAndReturn(teamMember.getTeam().getId());
        teamValidator.validateCheckTeamLeader(teamMember.getTeam(), requesterMemberId);


        TeamMatchAcceptRequest teamMatchAcceptRequest = TeamMatchAcceptRequest.of(teamMatch, member, teamMember.getTeam());

        teamMatchAcceptRequestRepository.save(teamMatchAcceptRequest);
        return TeamMatchAcceptRequestResponse.of(teamMatch, teamMatchAcceptRequest);
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
