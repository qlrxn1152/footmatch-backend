package com.dhoon.footmatch.team;

import com.dhoon.footmatch.member.dto.response.MemberCreateResponse;
import com.dhoon.footmatch.member.exception.exceptions.NotFoundMemberException;
import com.dhoon.footmatch.support.IntegrateTest;
import com.dhoon.footmatch.support.fixture.MemberFixture;
import com.dhoon.footmatch.support.fixture.TeamFixture;
import com.dhoon.footmatch.team.domain.Team;
import com.dhoon.footmatch.team.dto.request.TeamNameChangeRequest;
import com.dhoon.footmatch.team.dto.response.TeamNameChangeResponse;
import com.dhoon.footmatch.team.exception.exceptions.DuplicateTeamNameException;
import com.dhoon.footmatch.team.exception.exceptions.InvalidTeamNameException;
import com.dhoon.footmatch.team.exception.exceptions.NotFoundTeamException;
import com.dhoon.footmatch.team.exception.exceptions.SameTeamNameException;
import com.dhoon.footmatch.team.repository.TeamRepository;
import com.dhoon.footmatch.team.service.TeamService;
import com.dhoon.footmatch.teammember.repository.TeamMemberRepository;
import com.dhoon.footmatch.teammember.validation.exceptions.NotJoinedTeamException;
import com.dhoon.footmatch.teammember.validation.exceptions.NotTeamMemberException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.dhoon.footmatch.support.fixture.TeamFixture.*;
import static org.assertj.core.api.Assertions.*;

@IntegrateTest
public class TeamNameChangeTest {

    @Autowired private MemberFixture memberFixture;
    @Autowired private TeamFixture teamFixture;

    @Autowired private TeamService teamService;

    @Autowired private TeamMemberRepository teamMemberRepository;
    @Autowired private TeamRepository teamRepository;

    private TeamNameChangeRequest createTeamNameChangeRequest(String teamName) {
        return TeamNameChangeRequest.of(teamName);
    }

    @Test
    @DisplayName(value = "팀장은 팀 이름 변경에 성공")
    void changeTeamName() throws Exception {
        // given
        TeamFixtureData teamFixtureData = teamFixture.createTeamWithMember("userA", "teamA");

        // when
        TeamNameChangeResponse response = teamService.changeTeamName(createTeamNameChangeRequest("teamB"), teamFixtureData.leader().getMemberId(), teamFixtureData.team().getTeamId());
        Team savedTeam = teamRepository.findById(response.getTeamId()).get();

        // then
        assertThat(response.getTeamId()).isEqualTo(savedTeam.getId());
        assertThat(response.getNewTeamName()).isEqualTo("teamB");
        assertThat(savedTeam.getTeamName()).isEqualTo("teamB");
    }

    @Test
    @DisplayName(value = "팀장은 팀 이름 변경에 성공_정규화 확인")
    void changeTeamName_check_normalized() throws Exception {
        // given
        TeamFixtureData teamFixtureData = teamFixture.createTeamWithMember("userA", "teamA");

        // when
        TeamNameChangeResponse response = teamService.changeTeamName(createTeamNameChangeRequest(" Mo United  "), teamFixtureData.leader().getMemberId(), teamFixtureData.team().getTeamId());
        Team savedTeam = teamRepository.findById(response.getTeamId()).get();

        // then
        assertThat(response.getTeamId()).isEqualTo(savedTeam.getId());
        assertThat(response.getNewTeamName()).isEqualTo("Mo United");
        assertThat(savedTeam.getTeamName()).isEqualTo("Mo United");
    }

    @Test
    @DisplayName(value = "팀이름변경_실패_존재하지않는_회원")
    void changeTeamName_fail_notExistMember() throws Exception {
        // given
        TeamFixtureData teamFixtureData = teamFixture.createTeamWithMember("userA", "teamA");

        // when  && then
        assertThatThrownBy(() -> teamService.changeTeamName(createTeamNameChangeRequest("teamB"), 1234L, teamFixtureData.team().getTeamId()))
                .isInstanceOf(NotFoundMemberException.class)
                .hasMessage("멤버 조회 실패");
    }

    @Test
    @DisplayName(value = "팀이름변경_실패_존재하지않는_팀")
    void changeTeamName_fail_notExistTeam() throws Exception {
        // given
        TeamFixtureData teamFixtureData = teamFixture.createTeamWithMember("userA", "teamA");

        // when  && then
        assertThatThrownBy(() -> teamService.changeTeamName(createTeamNameChangeRequest("teamB"), teamFixtureData.leader().getMemberId(), 111L))
                .isInstanceOf(NotFoundTeamException.class)
                .hasMessage("팀 조회 실패");
    }

    @Test
    @DisplayName(value = "팀이름변경_실패_어떤팀에도_가입되지_않은_회원")
    void changeTeamName_fail_notJoinedTeam() throws Exception {
        // given
        TeamFixtureData teamFixtureData = teamFixture.createTeamWithMember("userA", "teamA");
        MemberCreateResponse memberB = memberFixture.signupMember("userB", "1234");

        // when  && then
        assertThatThrownBy(() -> teamService.changeTeamName(createTeamNameChangeRequest("teamB"), memberB.getMemberId(), teamFixtureData.team().getTeamId()))
                .isInstanceOf(NotJoinedTeamException.class)
                .hasMessage("팀에 속해있지 않은 회원입니다.");
    }

    @Test
    @DisplayName(value = "팀이름변경_실패_다른팀에_속해있는_회원")
    void changeTeamName_fail_not_teamMember() throws Exception {
        // given
        TeamFixtureData teamFixtureData = teamFixture.createTeamWithMember("userA", "teamA");
        TeamFixtureData teamBFixtureData = teamFixture.createTeamWithMember("userB", "teamB");

        // when  && then
        assertThatThrownBy(() -> teamService.changeTeamName(createTeamNameChangeRequest("change"), teamBFixtureData.leader().getMemberId(), teamFixtureData.team().getTeamId()))
                .isInstanceOf(NotTeamMemberException.class)
                .hasMessage("해당팀의 멤버가 아닙니다.");
    }

    @Test
    @DisplayName(value = "팀이름변경_실패_팀이름사이즈_부족")
    void changeTeamName_fail_invalid_team_name_short() throws Exception {
        // given
        TeamFixtureData teamFixtureData = teamFixture.createTeamWithMember("userA", "teamA");

        // when  && then
        assertThatThrownBy(() -> teamService.changeTeamName(createTeamNameChangeRequest("     a   "), teamFixtureData.leader().getMemberId(), teamFixtureData.team().getTeamId()))
                .isInstanceOf(InvalidTeamNameException.class)
                .hasMessage("팀 이름은 2~20자 까지만 허용합니다.");

        assertThatThrownBy(() -> teamService.changeTeamName(createTeamNameChangeRequest("a"), teamFixtureData.leader().getMemberId(), teamFixtureData.team().getTeamId()))
                .isInstanceOf(InvalidTeamNameException.class)
                .hasMessage("팀 이름은 2~20자 까지만 허용합니다.");
    }

    @Test
    @DisplayName(value = "팀이름변경_실패_팀이름사이즈_초과")
    void changeTeamName_fail_invalid_team_name_long() throws Exception {
        // given
        TeamFixtureData teamFixtureData = teamFixture.createTeamWithMember("userA", "teamA");

        // when  && then
        assertThatThrownBy(() -> teamService.changeTeamName(createTeamNameChangeRequest("      pdajoigfjnokclxnmvzoxcigjsopqweqewrt   "), teamFixtureData.leader().getMemberId(), teamFixtureData.team().getTeamId()))
                .isInstanceOf(InvalidTeamNameException.class)
                .hasMessage("팀 이름은 2~20자 까지만 허용합니다.");

        assertThatThrownBy(() -> teamService.changeTeamName(createTeamNameChangeRequest("asgijasdgjiasijfasdijfsdaijfdsaif"), teamFixtureData.leader().getMemberId(), teamFixtureData.team().getTeamId()))
                .isInstanceOf(InvalidTeamNameException.class)
                .hasMessage("팀 이름은 2~20자 까지만 허용합니다.");
    }

    @Test
    @DisplayName(value = "팀이름변경_실패_현재_팀이름과_동일")
    void changeTeamName_fail_same_current_name() throws Exception {
        // given
        TeamFixtureData teamFixtureData = teamFixture.createTeamWithMember("userA", "teamA");

        // when  && then
        assertThatThrownBy(() -> teamService.changeTeamName(createTeamNameChangeRequest("teamA"), teamFixtureData.leader().getMemberId(), teamFixtureData.team().getTeamId()))
                .isInstanceOf(SameTeamNameException.class)
                .hasMessage("팀 이름 변경사항이 없습니다.");

        assertThatThrownBy(() -> teamService.changeTeamName(createTeamNameChangeRequest("    teamA     "), teamFixtureData.leader().getMemberId(), teamFixtureData.team().getTeamId()))
                .isInstanceOf(SameTeamNameException.class)
                .hasMessage("팀 이름 변경사항이 없습니다.");
    }

    @Test
    @DisplayName(value = "팀이름변경_실패_다른팀이_이미_사용중인이름")
    void changeTeamName_fail_duplicate_team_name() throws Exception {
        // given
        TeamFixtureData teamFixtureData = teamFixture.createTeamWithMember("userA", "teamA");
        teamFixture.createTeamWithMember("userB", "teamB");

        // when  && then
        assertThatThrownBy(() -> teamService.changeTeamName(createTeamNameChangeRequest("teamB"), teamFixtureData.leader().getMemberId(), teamFixtureData.team().getTeamId()))
                .isInstanceOf(DuplicateTeamNameException.class)
                .hasMessage("해당 팀 이름이 이미 존재합니다.");

        assertThatThrownBy(() -> teamService.changeTeamName(createTeamNameChangeRequest("    teamB     "), teamFixtureData.leader().getMemberId(), teamFixtureData.team().getTeamId()))
                .isInstanceOf(DuplicateTeamNameException.class)
                .hasMessage("해당 팀 이름이 이미 존재합니다.");
    }




}
