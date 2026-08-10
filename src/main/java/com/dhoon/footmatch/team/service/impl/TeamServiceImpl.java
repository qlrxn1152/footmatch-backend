package com.dhoon.footmatch.team.service.impl;

import com.dhoon.footmatch.member.domain.Member;
import com.dhoon.footmatch.member.validation.MemberValidator;
import com.dhoon.footmatch.team.domain.Team;
import com.dhoon.footmatch.team.dto.request.TeamCreateRequest;
import com.dhoon.footmatch.team.dto.response.TeamCreateResponse;
import com.dhoon.footmatch.team.exception.exceptions.AlreadyJoinedTeamException;
import com.dhoon.footmatch.team.repository.TeamRepository;
import com.dhoon.footmatch.team.service.TeamService;
import com.dhoon.footmatch.team.validation.TeamValidator;
import com.dhoon.footmatch.teammember.domain.TeamMember;
import com.dhoon.footmatch.teammember.repository.TeamMemberRepository;
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
        String normalizedTeamName = request.getTeamName().strip();

        Member member = validateMemberCanCreateTeam(memberId);
        validateTeamNameForCreate(normalizedTeamName);

        Team team = createTeamAndAssignLeader(normalizedTeamName, member);
        return TeamCreateResponse.of(team);
    }












    // ================================================================== //
    private @NonNull Team createTeamAndAssignLeader(String teamName, Member member) {
        Team team = teamRepository.save(Team.createTeam(teamName));
        teamMemberRepository.save(TeamMember.createLeader(team, member));
        return team;
    }


    private void validateTeamNameForCreate(String teamName) {
        teamValidator.validateTeamNameNotDuplicated(teamName);
        teamValidator.validateTeamNameSize(teamName);
    }


    private Member validateMemberCanCreateTeam(Long memberId) {
        Member member = memberValidator.validateExistMemberAndReturn(memberId);
        if (teamMemberRepository.existsByMemberId(memberId)) {
            throw new AlreadyJoinedTeamException();
        }
        return member;
    }

}
