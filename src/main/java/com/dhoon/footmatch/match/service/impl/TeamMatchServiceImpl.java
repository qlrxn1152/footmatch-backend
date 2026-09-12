package com.dhoon.footmatch.match.service.impl;


import com.dhoon.footmatch.match.domain.TeamMatch;
import com.dhoon.footmatch.match.domain.TeamMatchStatus;
import com.dhoon.footmatch.match.dto.request.TeamMatchCreateRequest;
import com.dhoon.footmatch.match.dto.response.TeamMatchCreateResponse;
import com.dhoon.footmatch.match.exception.exceptions.AlreadyExistPendingMatchException;
import com.dhoon.footmatch.match.exception.exceptions.InvalidMatchPlayedAtException;
import com.dhoon.footmatch.match.repository.TeamMatchRepository;
import com.dhoon.footmatch.match.service.TeamMatchService;
import com.dhoon.footmatch.member.validation.MemberValidator;
import com.dhoon.footmatch.team.domain.Team;
import com.dhoon.footmatch.team.validation.TeamValidator;
import com.dhoon.footmatch.teammember.validation.TeamMemberValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class TeamMatchServiceImpl implements TeamMatchService {

    private final TeamMatchRepository teamMatchRepository;

    private final TeamValidator teamValidator;
    private final MemberValidator memberValidator;
    private final TeamMemberValidator teamMemberValidator;

    @Override
    public TeamMatchCreateResponse createTeamMatch(Long teamId, Long requesterMemberId, TeamMatchCreateRequest request) {
        Team team = teamValidator.validateExistTeamAndReturn(teamId);
        memberValidator.validateExistMemberAndReturn(requesterMemberId);
        teamMemberValidator.validateMemberBelongsToTeam(teamId, requesterMemberId);
        teamValidator.validateCheckTeamLeader(team, requesterMemberId);

        if (request.getPlayedAt() == null) {
            throw new InvalidMatchPlayedAtException();
        }

        if (!request.getPlayedAt().isAfter(LocalDateTime.now())) {
            throw new InvalidMatchPlayedAtException();
        }

        if (teamMatchRepository.existsByHomeTeamIdAndTeamMatchStatus(teamId, TeamMatchStatus.PENDING)) {
            throw new AlreadyExistPendingMatchException();
        }


        TeamMatch teamMatch = TeamMatch.createTeamMatch(team, request.getPlayedAt());
        teamMatchRepository.save(teamMatch);

        return TeamMatchCreateResponse.of(teamMatch);
    }






}
