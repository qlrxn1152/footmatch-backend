package com.dhoon.footmatch.team.service.impl;

import com.dhoon.footmatch.member.domain.Member;
import com.dhoon.footmatch.member.validation.MemberValidator;
import com.dhoon.footmatch.team.domain.Team;
import com.dhoon.footmatch.team.domain.TeamRole;
import com.dhoon.footmatch.team.dto.request.TeamCreateRequest;
import com.dhoon.footmatch.team.dto.request.TeamNameChangeRequest;
import com.dhoon.footmatch.team.dto.response.TeamCreateResponse;
import com.dhoon.footmatch.team.dto.response.TeamNameChangeResponse;
import com.dhoon.footmatch.team.exception.exceptions.AlreadyJoinedTeamException;
import com.dhoon.footmatch.team.exception.exceptions.NotTeamLeaderException;
import com.dhoon.footmatch.team.repository.TeamRepository;
import com.dhoon.footmatch.team.service.TeamService;
import com.dhoon.footmatch.team.validation.TeamValidator;
import com.dhoon.footmatch.teammember.domain.TeamMember;
import com.dhoon.footmatch.teammember.repository.TeamMemberRepository;
import com.dhoon.footmatch.teammember.validation.exceptions.NotJoinedTeamException;
import com.dhoon.footmatch.teammember.validation.exceptions.NotTeamMemberException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;

    private final MemberValidator memberValidator;
    private final TeamValidator teamValidator;

    @Override
    public TeamCreateResponse createTeam(TeamCreateRequest request, Long memberId) {
        String normalizedTeamName = normalizeTeamName(request.getTeamName());

        Member member = validateMemberCanCreateTeam(memberId);
        validateTeamNameForCreate(normalizedTeamName);

        Team team = createTeamAndAssignLeader(normalizedTeamName, member);
        return TeamCreateResponse.of(team);
    }


    @Override
    public TeamNameChangeResponse changeTeamName(TeamNameChangeRequest request, Long memberId, Long teamId) {
        String normalizedTeamName = normalizeTeamName(request.getTeamName());

        Team team = validateTeamNameChangePermission(memberId, teamId);
        validateTeamNameForChange(team.getTeamName(), normalizedTeamName);

        team.changeTeamName(normalizedTeamName);

        return TeamNameChangeResponse.of(team);
    }































    // ================================================================== //

    private Team createTeamAndAssignLeader(String teamName, Member member) {
        Team team = teamRepository.save(Team.createTeam(teamName));
        teamMemberRepository.save(TeamMember.createLeader(team, member));
        return team;
    }

    private void validateTeamNameForCreate(String teamName) {
        teamValidator.validateTeamNameSize(teamName);
        teamValidator.validateTeamNameNotDuplicated(teamName);
    }

    private void validateTeamNameForChange(String currentTeamName, String newTeamName) {
        teamValidator.validateTeamNameSize(newTeamName);
        teamValidator.validateSameTeamName(currentTeamName, newTeamName);
        teamValidator.validateTeamNameNotDuplicated(newTeamName);
    }


    private Member validateMemberCanCreateTeam(Long memberId) {
        Member member = memberValidator.validateExistMemberAndReturn(memberId);
        if (teamMemberRepository.existsByMemberId(memberId)) {
            throw new AlreadyJoinedTeamException();
        }
        return member;
    }


    private Team validateTeamNameChangePermission(Long memberId, Long teamId) {
        memberValidator.validateExistMember(memberId);
        Team team = teamValidator.validateExistTeamAndReturn(teamId);

        TeamMember teamMember = teamMemberRepository.findByMemberId(memberId)
                .orElseThrow(NotJoinedTeamException::new);

        if (!teamMember.getTeam().getId().equals(team.getId())) {
            throw new NotTeamMemberException();
        }

        if (teamMember.getTeamRole() != TeamRole.LEADER) {
            throw new NotTeamLeaderException();
        }

        return team;
    }

    private static String normalizeTeamName(String teamName) {
        return teamName.strip();
    }

}
