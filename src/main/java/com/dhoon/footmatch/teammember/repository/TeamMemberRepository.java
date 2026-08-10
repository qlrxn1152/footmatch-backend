package com.dhoon.footmatch.teammember.repository;

import com.dhoon.footmatch.teammember.domain.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {

    boolean existsByMemberId(Long memberId);
}
