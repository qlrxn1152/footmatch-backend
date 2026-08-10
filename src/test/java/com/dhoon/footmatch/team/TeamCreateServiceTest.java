package com.dhoon.footmatch.team;

import com.dhoon.footmatch.member.dto.response.MemberCreateResponse;
import com.dhoon.footmatch.support.IntegrateTest;
import com.dhoon.footmatch.support.fixture.MemberFixture;
import com.dhoon.footmatch.team.domain.Team;
import com.dhoon.footmatch.team.domain.TeamRole;
import com.dhoon.footmatch.team.dto.request.TeamCreateRequest;
import com.dhoon.footmatch.team.dto.response.TeamCreateResponse;
import com.dhoon.footmatch.team.exception.exceptions.AlreadyJoinedTeamException;
import com.dhoon.footmatch.team.exception.exceptions.DuplicateTeamNameException;
import com.dhoon.footmatch.team.exception.exceptions.InvalidTeamNameException;
import com.dhoon.footmatch.team.repository.TeamRepository;
import com.dhoon.footmatch.team.service.TeamService;

import com.dhoon.footmatch.teammember.repository.TeamMemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@IntegrateTest
class TeamCreateServiceTest {

    @Autowired private MemberFixture memberFixture;

    @Autowired private TeamService teamService;

    @Autowired private TeamMemberRepository teamMemberRepository;
    @Autowired private TeamRepository teamRepository;

    @Test
    @DisplayName(value = "팀에 속하지 않은 회원은, 팀을 생성할 수 있다.")
    void createTeam() throws Exception {
        // given
        MemberCreateResponse leaderMember = memberFixture.signupMember("userA", "1234");

        // when
        TeamCreateResponse response = teamService.createTeam(TeamCreateRequest.of("teamA"), leaderMember.getMemberId());
        Team savedTeam = teamRepository.findById(response.getTeamId()).get();

        // then
        assertThat(response.getTeamId()).isEqualTo(savedTeam.getId());
        assertThat(savedTeam.getTeamName()).isEqualTo("teamA");
        assertThat(teamMemberRepository.findByMemberId(leaderMember.getMemberId()).get().getTeamRole()).isEqualTo(TeamRole.LEADER);
        assertThat(teamMemberRepository.findByMemberId(leaderMember.getMemberId()).get().getTeam().getId()).isEqualTo(savedTeam.getId());
    }

    @Test
    @DisplayName(value = "팀 이름에는 앞뒤공백은 제거되고, 중간에는 공백이 포함될 수 있다.")
    void createTeam_can_middle_empty() throws Exception {
        // given
        MemberCreateResponse leaderMember = memberFixture.signupMember("userA", "1234");

        // when
        TeamCreateResponse response = teamService.createTeam(TeamCreateRequest.of("  Mo United "), leaderMember.getMemberId());
        Team savedTeam = teamRepository.findById(response.getTeamId()).get();

        // then
        assertThat(response.getTeamId()).isEqualTo(savedTeam.getId());
        assertThat(savedTeam.getTeamName()).isEqualTo("Mo United");
        assertThat(teamMemberRepository.findByMemberId(leaderMember.getMemberId()).get().getTeamRole()).isEqualTo(TeamRole.LEADER);
        assertThat(teamMemberRepository.findByMemberId(leaderMember.getMemberId()).get().getTeam().getId()).isEqualTo(savedTeam.getId());
    }

    @Test
    @DisplayName(value = "팀에 속한 회원은, 팀을 생성할 수 없다.")
    void createTeam_fail_already_joinedTeam() throws Exception {
        // given
        MemberCreateResponse leaderMember = memberFixture.signupMember("userA", "1234");
        teamService.createTeam(TeamCreateRequest.of("teamA"), leaderMember.getMemberId());

        // when && then
        assertThatThrownBy(() -> teamService.createTeam(TeamCreateRequest.of("teamA"), leaderMember.getMemberId()))
                .isInstanceOf(AlreadyJoinedTeamException.class)
                .hasMessage("팀에 이미 속한 회원입니다.");
    }

    @Test
    @DisplayName(value = "팀 이름은 중복될 수 없다.")
    void createTeam_fail_duplicateTeamName() throws Exception {
        // given
        MemberCreateResponse leaderMember = memberFixture.signupMember("userA", "1234");
        MemberCreateResponse leaderMemberB = memberFixture.signupMember("userB", "1234");
        teamService.createTeam(TeamCreateRequest.of("teamA"), leaderMember.getMemberId());

        // when && then
        assertThatThrownBy(() -> teamService.createTeam(TeamCreateRequest.of("teamA"), leaderMemberB.getMemberId()))
                .isInstanceOf(DuplicateTeamNameException.class)
                .hasMessage("해당 팀 이름이 이미 존재합니다.");
    }

    @Test
    @DisplayName(value = "팀 이름이 2글자 미만이면, 팀 생성에 실패해야한다.")
    void createTeam_fail_invalid_teamName_length_short() throws Exception {
        // given
        MemberCreateResponse leaderMember = memberFixture.signupMember("userA", "1234");

        // when && then
        assertThatThrownBy(() -> teamService.createTeam(TeamCreateRequest.of("t"), leaderMember.getMemberId()))
                .isInstanceOf(InvalidTeamNameException.class)
                .hasMessage("팀 이름은 2~20자 까지만 허용합니다.");
    }

    @Test
    @DisplayName(value = "팀 이름이 10글자 초과면, 팀 생성에 실패해야한다.")
    void createTeam_fail_invalid_teamName_length_long() throws Exception {
        // given
        MemberCreateResponse leaderMember = memberFixture.signupMember("userA", "1234");

        // when && then
        assertThatThrownBy(() -> teamService.createTeam(TeamCreateRequest.of("Manchester Unitedaaafdsafasdfasgasdfasdf"), leaderMember.getMemberId()))
                .isInstanceOf(InvalidTeamNameException.class)
                .hasMessage("팀 이름은 2~20자 까지만 허용합니다.");
    }

}
