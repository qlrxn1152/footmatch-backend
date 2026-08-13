package com.dhoon.footmatch.teammember.validation;

import com.dhoon.footmatch.team.exception.exceptions.AlreadyJoinedTeamException;
import com.dhoon.footmatch.teammember.exception.exceptions.NotTeamMemberException;
import com.dhoon.footmatch.teammember.repository.TeamMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TeamMemberValidator {

    private final TeamMemberRepository teamMemberRepository;

    public void validateMemberBelongsToTeam(Long teamId, Long memberId) {
        if (teamMemberRepository.existsByTeamIdAndMemberId(teamId, memberId)) {
            throw new NotTeamMemberException();
        }
    }

    public void validateMemberNotJoinedTeam(Long memberId) {
        if (teamMemberRepository.existsByMemberId(memberId)) {
            throw new AlreadyJoinedTeamException();
        }
    }
}
