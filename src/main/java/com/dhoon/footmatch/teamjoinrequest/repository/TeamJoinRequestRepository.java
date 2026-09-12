package com.dhoon.footmatch.teamjoinrequest.repository;

import com.dhoon.footmatch.teamjoinrequest.domain.TeamJoinRequest;
import com.dhoon.footmatch.teamjoinrequest.domain.TeamJoinRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TeamJoinRequestRepository extends JpaRepository<TeamJoinRequest, Long> {

    boolean existsByTeamIdAndMemberIdAndStatus(Long teamId, Long memberId, TeamJoinRequestStatus status);

    List<TeamJoinRequest> findAllByMemberIdAndStatus(Long memberId, TeamJoinRequestStatus status);

    @Query("select tjr from TeamJoinRequest tjr join fetch tjr.member where tjr.team.id = :teamId and tjr.status = :status order by tjr.createdAt asc")

    List<TeamJoinRequest> findAllByTeamIdAndStatus(Long teamId, TeamJoinRequestStatus status);


}
