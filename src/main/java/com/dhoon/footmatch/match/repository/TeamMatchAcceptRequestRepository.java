package com.dhoon.footmatch.match.repository;

import com.dhoon.footmatch.match.domain.TeamMatchAcceptRequest;
import com.dhoon.footmatch.match.domain.TeamMatchAcceptRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TeamMatchAcceptRequestRepository extends JpaRepository<TeamMatchAcceptRequest, Long> {

    boolean existsByTeamMatchIdAndTeamIdAndStatus(Long teamMatchId, Long teamId, TeamMatchAcceptRequestStatus status);

    @Query("select tmar from TeamMatchAcceptRequest tmar join fetch tmar.member join fetch tmar.team where tmar.teamMatch.homeTeam.id = :teamId")
    List<TeamMatchAcceptRequest> findAllByTeamId(Long teamId); // 홈팀 id


}
