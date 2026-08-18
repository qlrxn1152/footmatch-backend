package com.dhoon.footmatch.teamjoinrequest;

import com.dhoon.footmatch.member.dto.response.MemberCreateResponse;
import com.dhoon.footmatch.member.exception.exceptions.NotFoundMemberException;
import com.dhoon.footmatch.support.IntegrateTest;
import com.dhoon.footmatch.support.fixture.MemberFixture;
import com.dhoon.footmatch.support.fixture.TeamFixture;
import com.dhoon.footmatch.team.exception.exceptions.AlreadyJoinedTeamException;
import com.dhoon.footmatch.team.exception.exceptions.NotFoundTeamException;
import com.dhoon.footmatch.teamjoinrequest.domain.TeamJoinRequestStatus;
import com.dhoon.footmatch.teamjoinrequest.dto.response.TeamJoinRequestResponse;
import com.dhoon.footmatch.teamjoinrequest.exception.exceptions.DuplicateTeamJoinRequestException;
import com.dhoon.footmatch.teamjoinrequest.repository.TeamJoinRequestRepository;
import com.dhoon.footmatch.teamjoinrequest.service.TeamJoinRequestService;
import com.dhoon.footmatch.teammember.repository.TeamMemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.dhoon.footmatch.support.fixture.TeamFixture.*;
import static org.assertj.core.api.Assertions.*;

@IntegrateTest
class TeamJoinRequestTest {

    @Autowired private TeamFixture teamFixture;
    @Autowired private MemberFixture memberFixture;

    @Autowired private TeamJoinRequestService teamJoinRequestService;
    @Autowired private TeamJoinRequestRepository teamJoinRequestRepository;
    @Autowired private TeamMemberRepository teamMemberRepository;

    @Test
    @DisplayName(value = "팀에 가입하지 않은 회원은, 특정 팀에 가입신청 할 수있다.")
    void joinRequest() throws Exception {
        // given
        TeamFixtureData leader = teamFixture.createTeamWithLeaderMember("userA", "teamA");
        MemberCreateResponse requester = memberFixture.signupMember("requester", "1234");

        // when
        TeamJoinRequestResponse response = teamJoinRequestService.joinRequest(leader.team().getTeamId(), requester.getMemberId());

        // then
        assertThat(teamJoinRequestRepository.count()).isEqualTo(1);
        assertThat(teamJoinRequestRepository.findById(response.getJoinRequestId()).get().getId()).isEqualTo(response.getJoinRequestId());
        assertThat(teamJoinRequestRepository.existsByTeamIdAndMemberIdAndStatus(leader.team().getTeamId(), requester.getMemberId(), TeamJoinRequestStatus.PENDING)).isTrue();
        assertThat(teamMemberRepository.findByMemberId(requester.getMemberId()).isEmpty()).isTrue();
    }

    @Test
    @DisplayName(value = "팀에 속한 회원은 팀에 가입신청 할 수없다.")
    void joinRequest_fail_alreadyJoinedTeam() throws Exception {
        // given
        TeamFixtureData leader = teamFixture.createTeamWithLeaderMember("userA", "teamA");
        TeamFixtureData requester = teamFixture.createTeamWithLeaderMember("requester", "teamB");

        // when && then
        assertThatThrownBy(() -> teamJoinRequestService.joinRequest(leader.team().getTeamId(), requester.leader().getMemberId()))
                .isInstanceOf(AlreadyJoinedTeamException.class)
                .hasMessage("팀에 이미 속한 회원입니다.");
    }

    @Test
    @DisplayName(value = "이미 같은팀에 PENDING 가입신청이 존재하는경우, 해당팀에 추가로 가입신청을 넣을 수 없다.")
    void joinRequest_fail_rePending() throws Exception {
        // given
        TeamFixtureData leader = teamFixture.createTeamWithLeaderMember("userA", "teamA");
        MemberCreateResponse requester = memberFixture.signupMember("requester", "1234");
        teamJoinRequestService.joinRequest(leader.team().getTeamId(), requester.getMemberId());

        // when && then
        assertThatThrownBy(() -> teamJoinRequestService.joinRequest(leader.team().getTeamId(), requester.getMemberId()))
                .isInstanceOf(DuplicateTeamJoinRequestException.class)
                .hasMessage("이미 해당팀에 가입신청이 있습니다.");
    }

    @Test
    @DisplayName(value = "존재하지 않는 회원")
    void joinRequest_fail_not_exist_member() throws Exception {
        // given
        TeamFixtureData leader = teamFixture.createTeamWithLeaderMember("userA", "teamA");

        // when && then
        assertThatThrownBy(() -> teamJoinRequestService.joinRequest(leader.team().getTeamId(), 2134L))
                .isInstanceOf(NotFoundMemberException.class)
                .hasMessage("멤버 조회 실패");
    }

    @Test
    @DisplayName(value = "존재하지 않는 팀")
    void joinRequest_fail_not_exist_team() throws Exception {
        // given
        MemberCreateResponse requester = memberFixture.signupMember("userA", "1234");

        // when && then
        assertThatThrownBy(() -> teamJoinRequestService.joinRequest(1234L, requester.getMemberId()))
                .isInstanceOf(NotFoundTeamException.class)
                .hasMessage("팀 조회 실패");
    }









}