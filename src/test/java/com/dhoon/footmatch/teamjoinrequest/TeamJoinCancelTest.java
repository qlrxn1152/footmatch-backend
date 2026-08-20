package com.dhoon.footmatch.teamjoinrequest;

import com.dhoon.footmatch.member.dto.response.MemberCreateResponse;
import com.dhoon.footmatch.member.exception.exceptions.NotFoundMemberException;
import com.dhoon.footmatch.support.IntegrateTest;
import com.dhoon.footmatch.support.fixture.MemberFixture;
import com.dhoon.footmatch.support.fixture.TeamFixture;
import com.dhoon.footmatch.support.fixture.TeamJoinRequestFixture;
import com.dhoon.footmatch.team.domain.Team;
import com.dhoon.footmatch.team.repository.TeamRepository;
import com.dhoon.footmatch.teamjoinrequest.domain.TeamJoinRequestStatus;
import com.dhoon.footmatch.teamjoinrequest.dto.response.TeamJoinRequestCancelResponse;
import com.dhoon.footmatch.teamjoinrequest.dto.response.TeamJoinRequestResponse;
import com.dhoon.footmatch.teamjoinrequest.exception.exceptions.NotFoundTeamJoinRequestException;
import com.dhoon.footmatch.teamjoinrequest.exception.exceptions.NotTeamJoinRequestOwnerException;
import com.dhoon.footmatch.teamjoinrequest.exception.exceptions.TeamJoinRequestStatusException;
import com.dhoon.footmatch.teamjoinrequest.repository.TeamJoinRequestRepository;
import com.dhoon.footmatch.teamjoinrequest.service.TeamJoinRequestService;
import com.dhoon.footmatch.teammember.repository.TeamMemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@IntegrateTest
class TeamJoinCancelTest {

    @Autowired private TeamJoinRequestFixture teamJoinFixture;
    @Autowired private TeamFixture teamFixture;
    @Autowired private MemberFixture memberFixture;

    @Autowired private TeamJoinRequestService teamJoinRequestService;
    @Autowired private TeamJoinRequestRepository teamJoinRequestRepository;
    @Autowired private TeamMemberRepository teamMemberRepository;
    @Autowired private TeamRepository teamRepository;


    @Test
    @DisplayName(value = "가입신청자는 자신이 신청한 가입신청을 취소할 수 있다.")
    void cancel_teamJoinRequest() throws Exception {
        // given
        TeamJoinRequestResponse joinRequest = teamJoinFixture.createTeamJoinRequest("leaderA", "userA", "teamA");
        Team team = teamRepository.findById(joinRequest.getTeamId()).get();

        // when
        TeamJoinRequestCancelResponse response = teamJoinRequestService.cancelRequest(team.getId(), joinRequest.getJoinRequestId(), joinRequest.getMemberId());

        // then
        assertThat(teamJoinRequestRepository.findById(joinRequest.getJoinRequestId()).get().getStatus()).isEqualTo(TeamJoinRequestStatus.CANCELED);
        assertThat(response.getRequestId()).isEqualTo(joinRequest.getJoinRequestId());
        assertThat(response.getStatus()).isEqualTo(TeamJoinRequestStatus.CANCELED);
    }

    @Test
    @DisplayName(value = "멤버 미존재")
    void cancel_teamJoinRequest_fail_notFoundMember() throws Exception {
        // given
        TeamJoinRequestResponse joinRequest = teamJoinFixture.createTeamJoinRequest("leaderA", "userA", "teamA");
        Team team = teamRepository.findById(joinRequest.getTeamId()).get();

        // when && then
        assertThatThrownBy(() -> teamJoinRequestService.cancelRequest(team.getId(), joinRequest.getJoinRequestId(), 1234L))
                .isInstanceOf(NotFoundMemberException.class);
    }

    @Test
    @DisplayName(value = "해당 가입신청이 PENDING 이 아닌경우")
    void cancel_teamJoinRequest_fail_notPending() throws Exception {
        // given
        TeamJoinRequestResponse joinRequest = teamJoinFixture.createTeamJoinRequest("leaderA", "userA", "teamA");
        Team team = teamRepository.findById(joinRequest.getTeamId()).get();

        teamJoinRequestRepository.findById(joinRequest.getJoinRequestId()).get().acceptJoinRequest();

        // when && then
        assertThatThrownBy(() -> teamJoinRequestService.cancelRequest(team.getId(), joinRequest.getJoinRequestId(), joinRequest.getMemberId()))
                .isInstanceOf(TeamJoinRequestStatusException.class);
    }

    @Test
    @DisplayName(value = "가입신청이 존재하지 않을경우")
    void cancel_teamJoinRequest_fail_notExistRequest() throws Exception {
        // given
        TeamJoinRequestResponse joinRequest = teamJoinFixture.createTeamJoinRequest("leaderA", "userA", "teamA");
        Team team = teamRepository.findById(joinRequest.getTeamId()).get();

        // when && then
        assertThatThrownBy(() -> teamJoinRequestService.cancelRequest(team.getId(), 1113L, joinRequest.getMemberId()))
                .isInstanceOf(NotFoundTeamJoinRequestException.class);
    }

    @Test
    @DisplayName(value = "요청한 회원과 가입신청을 요청한 회원이 다를경우")
    void cancel_teamJoinRequest_fail_not_owner() throws Exception {
        // given
        TeamJoinRequestResponse joinRequest = teamJoinFixture.createTeamJoinRequest("leaderA", "userA", "teamA");
        MemberCreateResponse otherMember = memberFixture.signupMember("userB", "1234");
        Team team = teamRepository.findById(joinRequest.getTeamId()).get();

        // when && then
        assertThatThrownBy(() -> teamJoinRequestService.cancelRequest(team.getId(), joinRequest.getJoinRequestId(), otherMember.getMemberId()))
                .isInstanceOf(NotTeamJoinRequestOwnerException.class);
    }


}
