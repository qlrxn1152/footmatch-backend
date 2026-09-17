package com.dhoon.footmatch.match.domain;

import com.dhoon.footmatch.team.domain.Team;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "team_matches")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamMatch {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "home_team_id", nullable = false)
    private Team homeTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "away_team_id")
    private Team awayTeam;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "played_at", nullable = false)
    private LocalDateTime playedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "team_match_status", nullable = false)
    private TeamMatchStatus teamMatchStatus;

    private TeamMatch(Team homeTeam, LocalDateTime playedAt) {
        this.homeTeam = homeTeam;
        this.playedAt = playedAt;

        this.awayTeam = null; // TODO : null... ?

        this.createdAt = LocalDateTime.now();
        this.teamMatchStatus = TeamMatchStatus.PENDING;
    }

    public static TeamMatch createTeamMatch(Team homeTeam, LocalDateTime playedAt) {
        return new TeamMatch(homeTeam, playedAt);
    }


    public void match(Team awayTeam) {
        this.awayTeam = awayTeam;
        this.teamMatchStatus = TeamMatchStatus.MATCHED;
    }

    public void completed() {
        this.teamMatchStatus = TeamMatchStatus.COMPLETED;
    }

}
