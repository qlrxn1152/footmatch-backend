package com.dhoon.footmatch.match.domain;


import com.dhoon.footmatch.team.domain.Team;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "team_match_results")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamMatchResult {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_match_result_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_match_id", nullable = false, unique = true)
    private TeamMatch teamMatch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "winner_team_id", nullable = false)
    private Team winnerTeam;

    @Column(name = "home_score")
    private int homeScore;

    @Column(name = "away_score")
    private int awayScore;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "teamMatchResult")
    private List<TeamMatchScorer> scorers = new ArrayList<>();

    private TeamMatchResult(TeamMatch teamMatch, Team winnerTeam, int homeScore, int awayScore, List<TeamMatchScorer> scorers) {
        this.teamMatch = teamMatch;
        this.winnerTeam = winnerTeam;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.scorers = scorers;
    }

    public static TeamMatchResult of(TeamMatch teamMatch, Team winnerTeam, int homeScore, int awayScore, List<TeamMatchScorer> scorers) {
        return new TeamMatchResult(
                teamMatch,
                winnerTeam,
                homeScore,
                awayScore,
                scorers
        );
    }

}
