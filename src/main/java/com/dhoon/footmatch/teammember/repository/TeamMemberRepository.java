package com.dhoon.footmatch.teammember.repository;

import com.dhoon.footmatch.team.domain.TeamRole;
import com.dhoon.footmatch.teammember.domain.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {

    boolean existsByMemberId(Long memberId);

    Optional<TeamMember> findByMemberId(Long memberId);

    long countByTeamId(Long teamId);
}
