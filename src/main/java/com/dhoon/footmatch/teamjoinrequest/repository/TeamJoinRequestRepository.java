package com.dhoon.footmatch.teamjoinrequest.repository;

import com.dhoon.footmatch.teamjoinrequest.domain.TeamJoinRequest;
import com.dhoon.footmatch.teamjoinrequest.domain.TeamJoinRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamJoinRequestRepository extends JpaRepository<TeamJoinRequest, Long> {

    boolean existsByTeamIdAndMemberIdAndStatus(Long teamId, Long memberId, TeamJoinRequestStatus status);

    List<TeamJoinRequest> findAllByMemberIdAndStatus(Long memberId, TeamJoinRequestStatus status);


}
