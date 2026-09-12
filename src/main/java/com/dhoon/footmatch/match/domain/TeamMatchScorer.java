package com.dhoon.footmatch.match.domain;


import com.dhoon.footmatch.member.domain.Member;
import com.dhoon.footmatch.team.domain.Team;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "team_match_scorers")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamMatchScorer {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_match_scorer_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_match_result_id", nullable = false)
    private TeamMatchResult teamMatchResult;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @Column(name = "goal_count")
    private int goalCount;

    private TeamMatchScorer(TeamMatchResult teamMatchResult, Member member, Team team, int goalCount) {
        this.teamMatchResult = teamMatchResult;
        this.member = member;
        this.team = team;
        this.goalCount = goalCount;
    }

    public static TeamMatchScorer of(TeamMatchResult teamMatchResult, Member member, Team team, int goalCount) {
        return new TeamMatchScorer(
                teamMatchResult,
                member,
                team,
                goalCount
        );
    }
}
