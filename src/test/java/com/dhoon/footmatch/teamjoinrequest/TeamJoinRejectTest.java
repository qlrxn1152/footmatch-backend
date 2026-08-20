package com.dhoon.footmatch.teamjoinrequest;

import com.dhoon.footmatch.member.dto.response.MemberCreateResponse;
import com.dhoon.footmatch.support.IntegrateTest;
import com.dhoon.footmatch.support.fixture.MemberFixture;
import com.dhoon.footmatch.support.fixture.TeamFixture;
import com.dhoon.footmatch.support.fixture.TeamJoinRequestFixture;
import com.dhoon.footmatch.team.exception.exceptions.NotFoundTeamException;
import com.dhoon.footmatch.team.exception.exceptions.NotTeamLeaderException;
import com.dhoon.footmatch.team.repository.TeamRepository;
import com.dhoon.footmatch.teamjoinrequest.domain.TeamJoinRequestStatus;
import com.dhoon.footmatch.teamjoinrequest.dto.response.TeamJoinRequestRejectResponse;
import com.dhoon.footmatch.teamjoinrequest.dto.response.TeamJoinRequestResponse;
import com.dhoon.footmatch.teamjoinrequest.exception.exceptions.NotFoundTeamJoinRequestException;
import com.dhoon.footmatch.teamjoinrequest.exception.exceptions.NotTeamJoinRequestException;
import com.dhoon.footmatch.teamjoinrequest.exception.exceptions.TeamJoinRequestStatusException;
import com.dhoon.footmatch.teamjoinrequest.repository.TeamJoinRequestRepository;
import com.dhoon.footmatch.teamjoinrequest.service.TeamJoinRequestService;
import com.dhoon.footmatch.teammember.repository.TeamMemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.dhoon.footmatch.support.fixture.TeamFixture.*;
import static org.assertj.core.api.Assertions.*;

@IntegrateTest
class TeamJoinRejectTest {

    @Autowired private TeamJoinRequestFixture teamJoinFixture;
    @Autowired private TeamFixture teamFixture;
    @Autowired private MemberFixture memberFixture;

    @Autowired private TeamJoinRequestService teamJoinRequestService;
    @Autowired private TeamJoinRequestRepository teamJoinRequestRepository;
    @Autowired private TeamMemberRepository teamMemberRepository;
    @Autowired private TeamRepository teamRepository;

    @Test
    @DisplayName(value = "팀장은 자신의 팀에 들어온 가입요청을 거절할 수 있다.")
    void rejectRequest() throws Exception {
        // given
        TeamFixtureData team = teamFixture.createTeamWithLeaderMember("leaderA", "teamA");
        MemberCreateResponse member = memberFixture.signupMember("userA", "1234");

        TeamJoinRequestResponse joinRequest = teamJoinRequestService.joinRequest(team.team().getTeamId(), member.getMemberId());

        // when
        TeamJoinRequestRejectResponse response = teamJoinRequestService.rejectRequest(team.team().getTeamId(), joinRequest.getJoinRequestId(), team.leader().getMemberId());

        // then
        assertThat(teamJoinRequestRepository.existsByTeamIdAndMemberIdAndStatus(joinRequest.getTeamId(), joinRequest.getMemberId(), TeamJoinRequestStatus.REJECTED)).isTrue();
        assertThat(response.getRequestId()).isEqualTo(joinRequest.getJoinRequestId());
        assertThat(response.getStatus()).isEqualTo(TeamJoinRequestStatus.REJECTED);
        assertThat(teamJoinRequestRepository.findById(joinRequest.getJoinRequestId()).get().getStatus()).isEqualTo(TeamJoinRequestStatus.REJECTED);
    }

    @Test
    @DisplayName(value = "가입신청 팀과 맞지않는 팀")
    void rejectRequest_fail_notExistTeam() throws Exception {
        // given
        TeamFixtureData team = teamFixture.createTeamWithLeaderMember("leaderA", "teamA");
        MemberCreateResponse member = memberFixture.signupMember("userA", "1234");

        TeamJoinRequestResponse joinRequest = teamJoinRequestService.joinRequest(team.team().getTeamId(), member.getMemberId());

        // when && then
        assertThatThrownBy(() -> teamJoinRequestService.rejectRequest(1234L, joinRequest.getJoinRequestId(), team.leader().getMemberId()))
                .isInstanceOf(NotTeamJoinRequestException.class);
    }

    @Test
    @DisplayName(value = "가입신청이 PENDING 상태가 아닌경우, 가입신청을 거절할 수 없다.")
    void rejectRequest_fail_notPendingStatus() throws Exception {
        // given
        TeamFixtureData team = teamFixture.createTeamWithLeaderMember("leaderA", "teamA");
        MemberCreateResponse member = memberFixture.signupMember("userA", "1234");

        TeamJoinRequestResponse joinRequest = teamJoinRequestService.joinRequest(team.team().getTeamId(), member.getMemberId());
        teamJoinRequestRepository.findById(joinRequest.getJoinRequestId()).get().cancelJoinRequest();

        // when && then
        assertThatThrownBy(() -> teamJoinRequestService.rejectRequest(team.team().getTeamId(), joinRequest.getJoinRequestId(), team.leader().getMemberId()))
                .isInstanceOf(TeamJoinRequestStatusException.class);
    }

    @Test
    @DisplayName(value = "가입신청이 존재하지 않는경우에는 가입신청 거절할 수 없다.")
    void rejectRequest_fail_notExist_teamJoinRequest() throws Exception {
        // given
        TeamFixtureData team = teamFixture.createTeamWithLeaderMember("leaderA", "teamA");
        MemberCreateResponse member = memberFixture.signupMember("userA", "1234");

        TeamJoinRequestResponse joinRequest = teamJoinRequestService.joinRequest(team.team().getTeamId(), member.getMemberId());

        // when && then
        assertThatThrownBy(() -> teamJoinRequestService.rejectRequest(team.team().getTeamId(), 123124L, team.leader().getMemberId()))
                .isInstanceOf(NotFoundTeamJoinRequestException.class);
    }

    @Test
    @DisplayName(value = "해당팀의 팀장이 아닌경우 가입신청을 거절할 수 없다.")
    void rejectRequest_fail_not_teamLeader() throws Exception {
        // given
        TeamFixtureData team = teamFixture.createTeamWithLeaderMember("leaderA", "teamA");
        MemberCreateResponse member = memberFixture.signupMember("userA", "1234");
        MemberCreateResponse memberB = memberFixture.signupMember("userB", "1234");

        TeamJoinRequestResponse joinRequest = teamJoinRequestService.joinRequest(team.team().getTeamId(), member.getMemberId());
        teamJoinRequestService.acceptRequest(team.team().getTeamId(), joinRequest.getJoinRequestId(), team.leader().getMemberId());

        TeamJoinRequestResponse joinRequestB = teamJoinRequestService.joinRequest(team.team().getTeamId(), memberB.getMemberId());

        // when && then
        assertThatThrownBy(() -> teamJoinRequestService.rejectRequest(team.team().getTeamId(), joinRequestB.getJoinRequestId(), member.getMemberId()))
                .isInstanceOf(NotTeamLeaderException.class);
    }

    @Test
    @DisplayName(value = "가입신청에 있는팀과, 요청한 팀의 값이 다를경우 실패")
    void rejectRequest_fail_team() throws Exception {
        // given
        TeamFixtureData team = teamFixture.createTeamWithLeaderMember("leaderA", "teamA");
        MemberCreateResponse member = memberFixture.signupMember("userA", "1234");

        TeamJoinRequestResponse joinRequest = teamJoinRequestService.joinRequest(team.team().getTeamId(), member.getMemberId());

        // when && then
        assertThatThrownBy(() -> teamJoinRequestService.rejectRequest(1234L, joinRequest.getJoinRequestId(), member.getMemberId()))
                .isInstanceOf(NotTeamJoinRequestException.class);
    }






}
