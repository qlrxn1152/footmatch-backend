package com.dhoon.footmatch.team.domain;

import com.dhoon.footmatch.teammember.domain.TeamMember;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "teams")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {

    private static final int TEAM_RATING = 1000;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long id;

    @Column(name = "team_name", nullable = false, unique = true)
    private String teamName;

    @Column(name = "team_rating", nullable = false)
    private int rating;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    private Team(String teamName) {
        this.teamName = teamName;
        this.rating = TEAM_RATING;
        this.createdAt = LocalDateTime.now();
    }

    public static Team createTeam(String teamName) {
        return new Team(teamName);
    }
}
