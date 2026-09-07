package com.dhoon.footmatch.teammember.service.impl;

import com.dhoon.footmatch.support.IntegrateTest;
import com.dhoon.footmatch.support.fixture.TeamFixture;
import com.dhoon.footmatch.support.fixture.TeamJoinRequestFixture;
import com.dhoon.footmatch.team.exception.exceptions.NotFoundTeamException;
import com.dhoon.footmatch.teammember.dto.response.TeamMemberDto;
import com.dhoon.footmatch.teammember.dto.response.TeamMembersDto;
import com.dhoon.footmatch.teammember.service.TeamMemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.dhoon.footmatch.support.fixture.TeamFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@IntegrateTest
class TeamMemberServiceImplTest {

    @Autowired private TeamMemberService teamMemberService;

    @Autowired private TeamFixture teamFixture;
    @Autowired private TeamJoinRequestFixture teamJoinRequestFixture;

    @Test
    @DisplayName(value = "특정 팀에 속한 회원들을 조회할 수 있다.")
    void getTeamMembers() throws Exception {
        // given
        TeamFixtureData teamData = teamFixture.createTeamWithLeaderMember("leaderA", "teamA");
        teamJoinRequestFixture.requestCreateAndAccept(teamData.team().getTeamId(), "memberA");
        teamJoinRequestFixture.requestCreateAndAccept(teamData.team().getTeamId(), "memberB");
        teamJoinRequestFixture.requestCreateAndAccept(teamData.team().getTeamId(), "memberC");

        // when
        TeamMembersDto response = teamMemberService.getTeamMembers(teamData.team().getTeamId());

        // then
        assertThat(response.getTeamId()).isEqualTo(teamData.team().getTeamId());
        assertThat(response.getLeaderMemberUsername()).isEqualTo("leaderA");
        assertThat(response.getTeamMembers()).hasSize(4);
        assertThat(response.getTeamMembers()).extracting(TeamMemberDto::getUsername).containsExactly("leaderA", "memberA", "memberB", "memberC");
    }

    @Test
    @DisplayName(value = "존재하지 않는 팀에는 팀원을 조회할 수 없다.")
    void getTeamMembers_fail_notExist_team() throws Exception {
        // given
        TeamFixtureData teamData = teamFixture.createTeamWithLeaderMember("leaderA", "teamA");
        teamJoinRequestFixture.requestCreateAndAccept(teamData.team().getTeamId(), "memberA");
        teamJoinRequestFixture.requestCreateAndAccept(teamData.team().getTeamId(), "memberB");
        teamJoinRequestFixture.requestCreateAndAccept(teamData.team().getTeamId(), "memberC");

        // when && then
        assertThatThrownBy(() -> teamMemberService.getTeamMembers(1234L))
                .isInstanceOf(NotFoundTeamException.class);
    }



}