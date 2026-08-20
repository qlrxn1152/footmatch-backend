package com.dhoon.footmatch.teamjoinrequest;

import com.dhoon.footmatch.member.dto.response.MemberCreateResponse;
import com.dhoon.footmatch.support.IntegrateTest;
import com.dhoon.footmatch.support.fixture.MemberFixture;
import com.dhoon.footmatch.support.fixture.TeamFixture;
import com.dhoon.footmatch.support.fixture.TeamJoinRequestFixture;
import com.dhoon.footmatch.team.domain.Team;
import com.dhoon.footmatch.team.domain.TeamRole;
import com.dhoon.footmatch.team.dto.response.TeamCreateResponse;
import com.dhoon.footmatch.team.exception.exceptions.AlreadyJoinedTeamException;
import com.dhoon.footmatch.team.exception.exceptions.NotTeamLeaderException;
import com.dhoon.footmatch.team.repository.TeamRepository;
import com.dhoon.footmatch.teamjoinrequest.domain.TeamJoinRequest;
import com.dhoon.footmatch.teamjoinrequest.domain.TeamJoinRequestStatus;
import com.dhoon.footmatch.teamjoinrequest.dto.response.TeamJoinRequestAcceptResponse;
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
class TeamJoinAcceptTest {

    @Autowired private TeamJoinRequestFixture teamJoinFixture;
    @Autowired private TeamFixture teamFixture;
    @Autowired private MemberFixture memberFixture;

    @Autowired private TeamJoinRequestService teamJoinRequestService;
    @Autowired private TeamJoinRequestRepository teamJoinRequestRepository;
    @Autowired private TeamMemberRepository teamMemberRepository;
    @Autowired private TeamRepository teamRepository;

    @Test
    @DisplayName(value = "팀 가입신청 수락")
    void acceptTeamJoinRequest() throws Exception {
        // given
        TeamJoinRequestResponse joinRequest = teamJoinFixture.createTeamJoinRequest("leaderA", "userA", "teamA");
        Team team = teamRepository.findById(joinRequest.getTeamId()).get();

        // when
        TeamJoinRequestAcceptResponse response = teamJoinRequestService.acceptRequest(joinRequest.getTeamId(), joinRequest.getJoinRequestId(), team.getLeaderMember().getId());

        // then
        assertThat(response.getRequestId()).isEqualTo(joinRequest.getJoinRequestId());
        assertThat(response.getTeamId()).isEqualTo(joinRequest.getTeamId());
        assertThat(response.getMemberId()).isEqualTo(joinRequest.getMemberId());

        assertThat(teamJoinRequestRepository.findById(joinRequest.getJoinRequestId()).get().getStatus()).isEqualTo(TeamJoinRequestStatus.ACCEPTED);
        assertThat(teamMemberRepository.findByMemberId(joinRequest.getMemberId()).get().getMember().getId()).isEqualTo(joinRequest.getMemberId());
        assertThat(teamMemberRepository.findByMemberId(joinRequest.getMemberId()).get().getTeam().getId()).isEqualTo(joinRequest.getTeamId());
        assertThat(teamMemberRepository.findByMemberId(joinRequest.getMemberId()).get().getTeamRole()).isEqualTo(TeamRole.MEMBER);
    }

    @Test
    @DisplayName(value = "존재하지 않는 팀 가입신청")
    void acceptTeamJoinRequest_fail_notExistRequest() throws Exception {
        // given
        TeamJoinRequestResponse joinRequest = teamJoinFixture.createTeamJoinRequest("leaderA", "userA", "teamA");
        Team team = teamRepository.findById(joinRequest.getTeamId()).get();

        // when && then
        assertThatThrownBy(() -> teamJoinRequestService.acceptRequest(joinRequest.getTeamId(), 1234L, team.getLeaderMember().getId()))
                .isInstanceOf(NotFoundTeamJoinRequestException.class);
    }

    @Test
    @DisplayName(value = "다른팀에 가입신청한 요청을 수락할경우 실패한다")
    void acceptTeamJoinRequest_fail_other_team() throws Exception {
        // given
        TeamJoinRequestResponse joinRequest = teamJoinFixture.createTeamJoinRequest("leaderA", "userA", "teamA");
        TeamFixtureData teamBAndLeader = teamFixture.createTeamWithLeaderMember("leaderB", "teamB");


        // when && then
        assertThatThrownBy(() -> teamJoinRequestService.acceptRequest(teamBAndLeader.team().getTeamId(), joinRequest.getJoinRequestId(), teamBAndLeader.leader().getMemberId()))
                .isInstanceOf(NotTeamJoinRequestException.class);
    }

    @Test
    @DisplayName(value = "PENDING 상태가 아닌 가입신청의 경우, 수락할 수 없다.")
    void acceptTeamJoinRequest_fail_not_pending() throws Exception {
        // given
        TeamJoinRequestResponse joinRequest = teamJoinFixture.createTeamJoinRequest("leaderA", "userA", "teamA");
        Team team = teamRepository.findById(joinRequest.getTeamId()).get();

        TeamJoinRequest savedJoinRequest = teamJoinRequestRepository.findById(joinRequest.getJoinRequestId()).get();
        savedJoinRequest.cancelJoinRequest();


        // when && then
        assertThatThrownBy(() -> teamJoinRequestService.acceptRequest(joinRequest.getTeamId(), joinRequest.getJoinRequestId(), team.getLeaderMember().getId()))
                .isInstanceOf(TeamJoinRequestStatusException.class);
    }

    @Test
    @DisplayName(value = "수락자가 팀장이 아닌경우, 수락할 수 없다.")
    void acceptTeamJoinRequest_fail_not_teamLeader() throws Exception {
        // given
        TeamJoinRequestResponse joinRequest = teamJoinFixture.createTeamJoinRequest("leaderA", "userA", "teamA");
        Team team = teamRepository.findById(joinRequest.getTeamId()).get();
        teamJoinRequestService.acceptRequest(joinRequest.getTeamId(), joinRequest.getJoinRequestId(),  team.getLeaderMember().getId());
        MemberCreateResponse member = memberFixture.signupMember("userB", "1234");

        TeamJoinRequestResponse joinRequestB = teamJoinRequestService.joinRequest(joinRequest.getTeamId(), member.getMemberId());
        teamJoinRequestService.acceptRequest(joinRequest.getTeamId(), joinRequestB.getJoinRequestId(), team.getLeaderMember().getId());

        MemberCreateResponse memberC = memberFixture.signupMember("userC", "1234");
        TeamJoinRequestResponse joinRequestC = teamJoinRequestService.joinRequest(joinRequest.getTeamId(), memberC.getMemberId());


        // when && then
        assertThatThrownBy(() -> teamJoinRequestService.acceptRequest(joinRequest.getTeamId(), joinRequestC.getJoinRequestId(), joinRequestB.getMemberId()))
                .isInstanceOf(NotTeamLeaderException.class);
    }

    @Test
    @DisplayName(value = "가입신청을 요청한자가 수락당시 팀에 속해있을경우, 해당 가입신청은 무효가된다.")
    void acceptTeamJoinRequest_fail_already_joinTeam() throws Exception {
        // given
        TeamJoinRequestResponse joinRequest = teamJoinFixture.createTeamJoinRequest("leaderA", "userA", "teamA");

        Team team = teamRepository.findById(joinRequest.getTeamId()).get();
        TeamFixtureData teamB = teamFixture.createTeamWithLeaderMember("leaderB", "teamB");

        // userA -> teamB 에 가입
        TeamJoinRequestResponse joinToB = teamJoinRequestService.joinRequest(teamB.team().getTeamId(), joinRequest.getMemberId());

        teamJoinRequestService.acceptRequest(joinToB.getTeamId(), joinToB.getJoinRequestId(), teamB.leader().getMemberId());

        // when && then
        assertThatThrownBy(() -> teamJoinRequestService.acceptRequest(team.getId(), joinRequest.getJoinRequestId(), joinRequest.getMemberId()))
                .isInstanceOf(TeamJoinRequestStatusException.class);
    }









}
