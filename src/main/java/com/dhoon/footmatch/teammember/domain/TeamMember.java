package com.dhoon.footmatch.teammember.domain;

import com.dhoon.footmatch.member.domain.Member;
import com.dhoon.footmatch.team.domain.Team;
import com.dhoon.footmatch.team.domain.TeamRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "team_members")
public class TeamMember {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_member_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, unique = true)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @Column(name = "joined_at", nullable = false)
    private LocalDateTime joinedAt;

    @Enumerated(EnumType.STRING)
    @JoinColumn(name = "team_role", nullable = false)
    private TeamRole teamRole;

    private TeamMember(Member member, Team team, TeamRole teamRole) {
        this.member = member;
        this.team = team;
        this.teamRole = teamRole;

        this.joinedAt = LocalDateTime.now();
    }

    public static TeamMember createLeader(Team team, Member member) {
        return new TeamMember(member, team, TeamRole.LEADER);
    }

    public static TeamMember createMember(Team team, Member member) {
        return  new TeamMember(member, team, TeamRole.MEMBER);
    }

    public static TeamMember createStaff(Team team, Member member) {
        return new TeamMember(member, team, TeamRole.STAFF);
    }



}
