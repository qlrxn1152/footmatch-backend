package com.dhoon.footmatch.match.repository;

import com.dhoon.footmatch.match.domain.TeamMatchAcceptRequest;
import com.dhoon.footmatch.match.domain.TeamMatchAcceptRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamMatchAcceptRequestRepository extends JpaRepository<TeamMatchAcceptRequest, Long> {

    boolean existsByTeamMatchIdAndTeamIdAndStatus(Long teamMatchId, Long teamId, TeamMatchAcceptRequestStatus status);

}
