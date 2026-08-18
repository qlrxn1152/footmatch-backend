package com.dhoon.footmatch.support.fixture;

import com.dhoon.footmatch.member.dto.response.MemberCreateResponse;
import com.dhoon.footmatch.team.dto.request.TeamCreateRequest;
import com.dhoon.footmatch.team.dto.response.TeamCreateResponse;
import com.dhoon.footmatch.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TeamFixture {

    private final TeamService teamService;
    private final MemberFixture memberFixture;

    public TeamCreateResponse createTeam(String teamName, Long leaderMemberId) {
        return teamService.createTeam(TeamCreateRequest.of(teamName), leaderMemberId);
    }

    public TeamFixtureData createTeamWithLeaderMember(String username, String teamName) {
        MemberCreateResponse leader = memberFixture.signupMember(username, "1234");
        TeamCreateResponse team = createTeam(teamName, leader.getMemberId());

        return new TeamFixtureData(leader, team);
    }


    public record TeamFixtureData(
            MemberCreateResponse leader,
            TeamCreateResponse team
    ) { }

}
