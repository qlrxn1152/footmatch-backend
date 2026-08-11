package com.dhoon.footmatch.team.validation;

import com.dhoon.footmatch.team.domain.Team;
import com.dhoon.footmatch.team.exception.exceptions.DuplicateTeamNameException;
import com.dhoon.footmatch.team.exception.exceptions.InvalidTeamNameException;
import com.dhoon.footmatch.team.exception.exceptions.NotFoundTeamException;
import com.dhoon.footmatch.team.exception.exceptions.SameTeamNameException;
import com.dhoon.footmatch.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TeamValidator {

    private final TeamRepository teamRepository;


    public void validateTeamNameNotDuplicated(String teamName) {
        if (teamRepository.existsByTeamName(teamName)) {
            throw new DuplicateTeamNameException();
        }
    }

    public void validateTeamNameSize(String teamName) {
        if (teamName.length() < 2 || teamName.length() > 20) {
            throw new InvalidTeamNameException();
        }
    }

    public void validateSameTeamName(String currentTeamName, String newTeamName) {
        if (currentTeamName.equals(newTeamName)) {
            throw new SameTeamNameException();
        }
    }

    public Team validateExistTeamAndReturn(Long teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(NotFoundTeamException::new);
    }


}
