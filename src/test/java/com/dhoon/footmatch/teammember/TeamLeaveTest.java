package com.dhoon.footmatch.teammember;

import com.dhoon.footmatch.member.dto.response.MemberCreateResponse;
import com.dhoon.footmatch.member.exception.exceptions.NotFoundMemberException;
import com.dhoon.footmatch.support.IntegrateTest;
import com.dhoon.footmatch.support.fixture.MemberFixture;
import com.dhoon.footmatch.support.fixture.TeamFixture;
import com.dhoon.footmatch.support.fixture.TeamJoinRequestFixture;
import com.dhoon.footmatch.team.exception.exceptions.NotFoundTeamException;
import com.dhoon.footmatch.team.repository.TeamRepository;
import com.dhoon.footmatch.teamjoinrequest.dto.response.TeamJoinRequestAcceptResponse;
import com.dhoon.footmatch.teamjoinrequest.repository.TeamJoinRequestRepository;
import com.dhoon.footmatch.teamjoinrequest.service.TeamJoinRequestService;
import com.dhoon.footmatch.teammember.dto.response.TeamLeaveResponse;
import com.dhoon.footmatch.teammember.exception.exceptions.NotFoundTeamMemberException;
import com.dhoon.footmatch.teammember.exception.exceptions.NotTeamMemberException;
import com.dhoon.footmatch.teammember.exception.exceptions.TeamLeaderCannotLeaveException;
import com.dhoon.footmatch.teammember.repository.TeamMemberRepository;
import com.dhoon.footmatch.teammember.service.TeamMemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.*;

@IntegrateTest
public class TeamLeaveTest {

    @Autowired private TeamJoinRequestFixture teamJoinFixture;
    @Autowired private TeamFixture teamFixture;
    @Autowired private MemberFixture memberFixture;

    @Autowired private TeamMemberService teamMemberService;
    @Autowired private TeamMemberRepository teamMemberRepository;

    @Test
    @DisplayName(value = "팀에 가입한 팀원은 팀 탈퇴 가능")
    void leave_team() throws Exception {
        // given
        TeamFixture.TeamFixtureData data = teamFixture.createTeamWithLeaderMember("leaderA", "teamA");
        TeamJoinRequestAcceptResponse acceptResponse = teamJoinFixture.requestCreateAndAccept(data.team().getTeamId(), "memberA");

        // when
        TeamLeaveResponse response = teamMemberService.leaveTeam(data.team().getTeamId(), acceptResponse.getMemberId());

        // then
        assertThat(response.getTeamId()).isEqualTo(data.team().getTeamId());
        assertThat(response.getMemberId()).isEqualTo(acceptResponse.getMemberId());
        assertThat(teamMemberRepository.findByMemberId(response.getMemberId()).isEmpty()).isTrue();
    }

    @Test
    @DisplayName(value = "존재하지 않는 회원은 예외발생")
    void leave_team_fail_notExistMember() throws Exception {
        // given
        TeamFixture.TeamFixtureData data = teamFixture.createTeamWithLeaderMember("leaderA", "teamA");
        TeamJoinRequestAcceptResponse acceptResponse = teamJoinFixture.requestCreateAndAccept(data.team().getTeamId(), "memberA");

        // when && then
        assertThatThrownBy(() -> teamMemberService.leaveTeam(data.team().getTeamId(), 1234L))
                .isInstanceOf(NotFoundMemberException.class);

        assertThat(teamMemberRepository.findByMemberId(acceptResponse.getMemberId()).get().getMember().getId()).isEqualTo(acceptResponse.getMemberId());
    }

    @Test
    @DisplayName(value = "존재하지 않는 팀은 예외발생")
    void leave_team_fail_notExistTeam() throws Exception {
        // given
        TeamFixture.TeamFixtureData data = teamFixture.createTeamWithLeaderMember("leaderA", "teamA");
        TeamJoinRequestAcceptResponse acceptResponse = teamJoinFixture.requestCreateAndAccept(data.team().getTeamId(), "memberA");

        // when && then
        assertThatThrownBy(() -> teamMemberService.leaveTeam(12343L, acceptResponse.getMemberId()))
                .isInstanceOf(NotFoundTeamException.class);

        assertThat(teamMemberRepository.findByMemberId(acceptResponse.getMemberId()).get().getMember().getId()).isEqualTo(acceptResponse.getMemberId());
    }

    @Test
    @DisplayName(value = "팀에 속해있지 않는 회원은 탈퇴 불가능")
    void leave_team_fail_not_team_member() throws Exception {
        // given
        MemberCreateResponse member = memberFixture.signupMember("userB", "1234");
        TeamFixture.TeamFixtureData data = teamFixture.createTeamWithLeaderMember("leaderA", "teamA");

        // when && then
        assertThatThrownBy(() -> teamMemberService.leaveTeam(data.team().getTeamId(), member.getMemberId()))
                .isInstanceOf(NotFoundTeamMemberException.class);
    }



    @Test
    @DisplayName(value = "해당 팀 소속이 아닌경우 탈퇴 실패")
    void leave_team_fail_notTeamMember() throws Exception {
        // given
        TeamFixture.TeamFixtureData data = teamFixture.createTeamWithLeaderMember("leaderA", "teamA");
        TeamFixture.TeamFixtureData dataB = teamFixture.createTeamWithLeaderMember("leaderB", "teamB");

        TeamJoinRequestAcceptResponse acceptResponse = teamJoinFixture.requestCreateAndAccept(data.team().getTeamId(), "memberA");

        // when && then
        assertThatThrownBy(() -> teamMemberService.leaveTeam(dataB.team().getTeamId(), acceptResponse.getMemberId()))
                .isInstanceOf(NotTeamMemberException.class);

        assertThat(teamMemberRepository.findByMemberId(acceptResponse.getMemberId()).get().getMember().getId()).isEqualTo(acceptResponse.getMemberId());
    }

    @Test
    @DisplayName(value = "해당 팀 팀장은 탈퇴 불가능")
    void leave_team_fail_team_leader() throws Exception {
        // given
        TeamFixture.TeamFixtureData data = teamFixture.createTeamWithLeaderMember("leaderA", "teamA");

        // when && then
        assertThatThrownBy(() -> teamMemberService.leaveTeam(data.team().getTeamId(), data.leader().getMemberId()))
                .isInstanceOf(TeamLeaderCannotLeaveException.class);

        assertThat(teamMemberRepository.findByMemberId(data.leader().getMemberId()).get()).isNotNull();
    }









}
