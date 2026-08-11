package com.dhoon.footmatch.team;

import com.dhoon.footmatch.support.IntegrateTest;
import com.dhoon.footmatch.support.fixture.TeamFixture;
import com.dhoon.footmatch.team.dto.response.TeamDetailResponse;
import com.dhoon.footmatch.team.exception.exceptions.NotFoundTeamException;
import com.dhoon.footmatch.team.service.TeamService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.dhoon.footmatch.support.fixture.TeamFixture.*;
import static org.assertj.core.api.Assertions.*;

@IntegrateTest
public class TeamQueryTest {

    @Autowired private TeamFixture teamFixture;

    @Autowired private TeamService teamService;

    @Test
    @DisplayName(value = "팀 상세를 조회할 수 있다.")
    void getTeam() throws Exception {
        // given
        TeamFixtureData fixtureData = teamFixture.createTeamWithMember("userA", "teamA");

        // when
        TeamDetailResponse response = teamService.getTeam(fixtureData.team().getTeamId());

        // then
        assertThat(response.getTeamId()).isEqualTo(fixtureData.team().getTeamId());
        assertThat(response.getTeamName()).isEqualTo("teamA");
        assertThat(response.getTeamRating()).isEqualTo(1000);
        assertThat(response.getTeamMemberCount()).isEqualTo(1);
        assertThat(response.getLeaderId()).isEqualTo(fixtureData.leader().getMemberId());
    }

    @Test
    @DisplayName(value = "존재하지 않는 팀을 조회할경우, 팀 상세 조회에 실패한다.")
    void getTeam_fail_notExistTeam() throws Exception {
        // given
        teamFixture.createTeamWithMember("userA", "teamA");

        // when && then
        assertThatThrownBy(() -> teamService.getTeam(1234L))
                .isInstanceOf(NotFoundTeamException.class)
                .hasMessage("팀 조회 실패");
    }
}
