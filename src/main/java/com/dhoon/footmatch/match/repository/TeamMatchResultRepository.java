package com.dhoon.footmatch.match.repository;

import com.dhoon.footmatch.match.domain.TeamMatch;
import com.dhoon.footmatch.match.domain.TeamMatchResult;
import com.dhoon.footmatch.match.domain.TeamMatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TeamMatchResultRepository extends JpaRepository<TeamMatchResult, Long> {

    @Query("select tm from TeamMatch tm where (tm.homeTeam.id = :teamId or tm.awayTeam.id = :teamId) and tm.teamMatchStatus = :teamMatchStatus")
    List<TeamMatchResult> findAllTheTeamMatchedMatches(Long teamId, TeamMatchStatus teamMatchStatus);

    @Query("select tm from TeamMatch tm join fetch tm.homeTeam join fetch tm.homeTeam.leaderMember where tm.teamMatchStatus = :teamMatchStatus")
    List<TeamMatchResult> findAllByTeamMatchStatus(TeamMatchStatus teamMatchStatus);


}
