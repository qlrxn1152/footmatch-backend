package com.dhoon.footmatch.match.repository;

import com.dhoon.footmatch.match.domain.TeamMatch;
import com.dhoon.footmatch.match.domain.TeamMatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeamMatchRepository extends JpaRepository<TeamMatch, Long> {
    boolean existsByHomeTeamIdAndTeamMatchStatus(Long homeTeamId, TeamMatchStatus teamMatchStatus);

    @Query("select tm from TeamMatch tm join fetch tm.homeTeam join fetch tm.homeTeam.leaderMember where tm.teamMatchStatus = :teamMatchStatus")
    List<TeamMatch> findAllByTeamMatchStatus(TeamMatchStatus teamMatchStatus);

    @Query("select tm from TeamMatch tm join fetch tm.homeTeam where tm.homeTeam.id = :homeTeamId and tm.teamMatchStatus = :teamMatchStatus")
    List<TeamMatch> findAllByHomeTeamIdAndStatus(Long homeTeamId, TeamMatchStatus teamMatchStatus);

    @Query("select tm from TeamMatch tm where (tm.homeTeam.id = :teamId or tm.awayTeam.id = :teamId) and tm.teamMatchStatus = :teamMatchStatus")
    List<TeamMatch> findAllTheTeamMatchedMatches(Long teamId, TeamMatchStatus teamMatchStatus);

}
