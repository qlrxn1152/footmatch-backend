package com.dhoon.footmatch.match.validation;

import com.dhoon.footmatch.match.domain.TeamMatchStatus;
import com.dhoon.footmatch.match.dto.request.TeamMatchCreateRequest;
import com.dhoon.footmatch.match.exception.exceptions.AlreadyExistPendingMatchException;
import com.dhoon.footmatch.match.exception.exceptions.InvalidMatchPlayedAtException;
import com.dhoon.footmatch.match.repository.TeamMatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TeamMatchValidation {

    private final TeamMatchRepository teamMatchRepository;

    public void validatePlayedAt(TeamMatchCreateRequest request) {
        if (request.getPlayedAt() == null) {
            throw new InvalidMatchPlayedAtException();
        }

        if (!request.getPlayedAt().isAfter(LocalDateTime.now())) {
            throw new InvalidMatchPlayedAtException();
        }
    }

    public void validateAlreadyExistPendingMatch(Long teamId) {
        if (teamMatchRepository.existsByHomeTeamIdAndTeamMatchStatus(teamId, TeamMatchStatus.PENDING)) {
            throw new AlreadyExistPendingMatchException();
        }
    }


}
