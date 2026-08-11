package com.dhoon.footmatch.team.repository;

import com.dhoon.footmatch.team.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {

    boolean existsByTeamName(String teamName);

    @Query("select t from Team t join fetch t.leaderMember where t.id = :teamId")
    Optional<Team> findByTeamIdWithLeaderMember(@Param("teamId") Long teamId);
}
