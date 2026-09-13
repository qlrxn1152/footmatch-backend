package com.dhoon.footmatch.match.domain;

import com.dhoon.footmatch.member.domain.Member;
import com.dhoon.footmatch.team.domain.Team;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "team_match_accept_requests")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamMatchAcceptRequest {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_match_accept_request_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_match_id", nullable = false)
    private TeamMatch teamMatch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_team_id", nullable = false)
    private Team team;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "team_match_accept_requester_status", nullable = false)
    private TeamMatchAcceptRequestStatus status;

    private TeamMatchAcceptRequest(TeamMatch teamMatch, Member member, Team team) {
        this.teamMatch = teamMatch;
        this.member = member;
        this.team = team;

        this.createdAt = LocalDateTime.now();
        this.status = TeamMatchAcceptRequestStatus.PENDING;
    }

    public static TeamMatchAcceptRequest of(TeamMatch teamMatch, Member member, Team team) {
        return new TeamMatchAcceptRequest(teamMatch, member, team);
    }


}
