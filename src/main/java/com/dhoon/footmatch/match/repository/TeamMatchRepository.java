package com.dhoon.footmatch.match.repository;

import com.dhoon.footmatch.match.domain.TeamMatch;
import com.dhoon.footmatch.match.domain.TeamMatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TeamMatchRepository extends JpaRepository<TeamMatch, Long> {
    boolean existsByHomeTeamIdAndTeamMatchStatus(Long homeTeamId, TeamMatchStatus teamMatchStatus);

}
