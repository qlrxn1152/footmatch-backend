package com.dhoon.footmatch.teamjoinrequest;

import com.dhoon.footmatch.member.exception.exceptions.NotFoundMemberException;
import com.dhoon.footmatch.support.IntegrateTest;
import com.dhoon.footmatch.support.fixture.TeamFixture;
import com.dhoon.footmatch.support.fixture.TeamJoinRequestFixture;
import com.dhoon.footmatch.team.domain.Team;
import com.dhoon.footmatch.team.exception.exceptions.NotTeamLeaderException;
import com.dhoon.footmatch.team.repository.TeamRepository;
import com.dhoon.footmatch.team.validation.TeamValidator;
import com.dhoon.footmatch.teamjoinrequest.dto.response.TeamJoinRequestAcceptResponse;
import com.dhoon.footmatch.teamjoinrequest.dto.response.TeamJoinRequestListDto;
import com.dhoon.footmatch.teamjoinrequest.dto.response.TeamJoinRequestResponse;
import com.dhoon.footmatch.teamjoinrequest.dto.response.TeamJoinRequestsListResponse;
import com.dhoon.footmatch.teamjoinrequest.repository.TeamJoinRequestRepository;
import com.dhoon.footmatch.teamjoinrequest.service.TeamJoinRequestService;
import com.dhoon.footmatch.teammember.exception.exceptions.NotTeamMemberException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.*;

@IntegrateTest
public class TeamJoinRequestListTest {

    @Autowired private TeamJoinRequestFixture teamJoinRequestFixture;

    @Autowired private TeamJoinRequestService teamJoinRequestService;
    @Autowired private TeamFixture teamFixture;
    @Autowired private TeamValidator teamValidator;
    @Autowired private TeamJoinRequestRepository teamJoinRequestRepository;

    @Test
    @DisplayName(value = "팀장은 자신의 팀에 들어온 Pending 매치들을 조회할 수 있다.")
    void getPendingMatches() throws Exception {
        // given
        TeamJoinRequestResponse request1 = teamJoinRequestFixture.createTeamJoinRequest("leaderA", "memberA", "teamA");
        TeamJoinRequestResponse request2 = teamJoinRequestFixture.createTeamJoinRequest(request1.getTeamId(), "memberB");
        TeamJoinRequestResponse request3 = teamJoinRequestFixture.createTeamJoinRequest(request1.getTeamId(), "memberC");
        TeamJoinRequestResponse request4 = teamJoinRequestFixture.createTeamJoinRequest(request1.getTeamId(), "memberD");

        Team team = teamValidator.validateExistTeamAndReturn(request1.getTeamId());

        // when
        TeamJoinRequestsListResponse requests = teamJoinRequestService.getPendingRequests(team.getId(), team.getLeaderMember().getId());

        // then
        assertThat(requests.getTeamName()).isEqualTo("teamA");
        assertThat(requests.getRequests().size()).isEqualTo(4);
        assertThat(requests.getRequests()).extracting(TeamJoinRequestListDto::getUsername).containsExactly("memberA", "memberB", "memberC", "memberD");
    }

    @Test
    @DisplayName(value = "해당팀의 팀장이 아닌경우, 해당팀의 PENDING 가입요청들을 조회할 수 없다.")
    void getPendingMatches_fail_notTeamLeader() throws Exception {
        // given
        TeamJoinRequestResponse request1 = teamJoinRequestFixture.createTeamJoinRequest("leaderA", "memberA", "teamA");
        TeamJoinRequestResponse request2 = teamJoinRequestFixture.createTeamJoinRequest(request1.getTeamId(), "memberB");
        TeamJoinRequestResponse request3 = teamJoinRequestFixture.createTeamJoinRequest(request1.getTeamId(), "memberC");
        TeamJoinRequestResponse request4 = teamJoinRequestFixture.createTeamJoinRequest(request1.getTeamId(), "memberD");

        TeamFixture.TeamFixtureData teamB = teamFixture.createTeamWithLeaderMember("leaderB", "teamB");
        Team team = teamValidator.validateExistTeamAndReturn(request1.getTeamId());

        TeamJoinRequestAcceptResponse request5 = teamJoinRequestFixture.requestCreateAndAccept(request1.getTeamId(), "acceptMember");

        // when && then
        assertThatThrownBy(() -> teamJoinRequestService.getPendingRequests(team.getId(), 1234L))
                .isInstanceOf(NotFoundMemberException.class);

        assertThatThrownBy(() -> teamJoinRequestService.getPendingRequests(team.getId(), request2.getMemberId()))
                .isInstanceOf(NotTeamMemberException.class);

        assertThatThrownBy(() -> teamJoinRequestService.getPendingRequests(team.getId(), request5.getMemberId()))
                .isInstanceOf(NotTeamLeaderException.class);

        assertThatThrownBy(() -> teamJoinRequestService.getPendingRequests(team.getId(), teamB.leader().getMemberId()))
                .isInstanceOf(NotTeamMemberException.class);

        TeamJoinRequestsListResponse requests = teamJoinRequestService.getPendingRequests(team.getId(), team.getLeaderMember().getId());

        // then
        assertThat(requests.getTeamName()).isEqualTo("teamA");
        assertThat(requests.getRequests().size()).isEqualTo(4);
        assertThat(requests.getRequests()).extracting(TeamJoinRequestListDto::getUsername).containsExactly("memberA", "memberB", "memberC", "memberD");
    }




}
