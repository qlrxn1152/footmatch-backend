package com.dhoon.footmatch.teammember.validation;

import com.dhoon.footmatch.team.exception.exceptions.AlreadyJoinedTeamException;
import com.dhoon.footmatch.teammember.domain.TeamMember;
import com.dhoon.footmatch.teammember.exception.exceptions.NotFoundTeamMemberException;
import com.dhoon.footmatch.teammember.exception.exceptions.NotTeamMemberException;
import com.dhoon.footmatch.teammember.repository.TeamMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TeamMemberValidator {

    private final TeamMemberRepository teamMemberRepository;

    // 해당팀에 속해있지 않는경우
    public void validateMemberBelongsToTeam(Long teamId, Long memberId) {
        if (!teamMemberRepository.existsByTeamIdAndMemberId(teamId, memberId)) {
            throw new NotTeamMemberException();
        }
    }

    // 어떤팀에도 속해있지 않는경우
    public TeamMember validateMemberBelongsToTeamAndReturn(Long memberId) {
        return teamMemberRepository.findByMemberId(memberId)
                .orElseThrow(NotFoundTeamMemberException::new);
    }

    public void validateMemberNotJoinedTeam(Long memberId) {
        if (teamMemberRepository.existsByMemberId(memberId)) {
            throw new AlreadyJoinedTeamException();
        }
    }

}
