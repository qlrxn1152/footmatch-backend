package com.dhoon.footmatch.match.repository;

import com.dhoon.footmatch.match.domain.TeamMatchResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamMatchResultRepository extends JpaRepository<TeamMatchResult, Long> {
}
