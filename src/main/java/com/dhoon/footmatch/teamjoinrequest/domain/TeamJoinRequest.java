package com.dhoon.footmatch.teamjoinrequest.domain;

import com.dhoon.footmatch.member.domain.Member;
import com.dhoon.footmatch.team.domain.Team;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "team_join_requests")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamJoinRequest {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_join_request_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "team_join_request_status", nullable = false)
    private TeamJoinRequestStatus status;

    private TeamJoinRequest(Team team, Member member) {
        this.team = team;
        this.member = member;
        this.createdAt = LocalDateTime.now();
        this.status = TeamJoinRequestStatus.PENDING;
    }

    public static TeamJoinRequest createJoinRequest(Team team, Member member) {
        return new TeamJoinRequest(team, member);
    }

    public void acceptJoinRequest() {
        this.status = TeamJoinRequestStatus.ACCEPTED;
    }

    public void cancelJoinRequest() {
        this.status = TeamJoinRequestStatus.CANCELED;
    }

    public void rejectJoinRequest() {
        this.status = TeamJoinRequestStatus.REJECTED;
    }


}
