package com.dhoon.footmatch.match.repository;

import com.dhoon.footmatch.match.domain.TeamMatch;
import com.dhoon.footmatch.match.domain.TeamMatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamMatchRepository extends JpaRepository<TeamMatch, Long> {

    boolean existByTeamIdAndTeamMatchStatus(Long teamId, TeamMatchStatus teamMatchStatus);
}
