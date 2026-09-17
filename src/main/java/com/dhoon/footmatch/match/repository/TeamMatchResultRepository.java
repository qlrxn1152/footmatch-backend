package com.dhoon.footmatch.match.repository;

import com.dhoon.footmatch.match.domain.TeamMatch;
import com.dhoon.footmatch.match.domain.TeamMatchResult;
import com.dhoon.footmatch.match.domain.TeamMatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TeamMatchResultRepository extends JpaRepository<TeamMatchResult, Long> {

    @Query("select tmr from TeamMatchResult tmr where (tmr.teamMatch.homeTeam.id = :teamId or tmr.teamMatch.awayTeam.id = :teamId) and tmr.teamMatch.teamMatchStatus = :teamMatchStatus")
    List<TeamMatchResult> findAllTheTeamMatchedMatches(Long teamId, TeamMatchStatus teamMatchStatus);

    @Query("select tmr from TeamMatchResult tmr join fetch tmr.teamMatch.homeTeam join fetch tmr.teamMatch.homeTeam.leaderMember where tmr.teamMatch.teamMatchStatus = :teamMatchStatus")
    List<TeamMatchResult> findAllByTeamMatchStatus(TeamMatchStatus teamMatchStatus);


}
