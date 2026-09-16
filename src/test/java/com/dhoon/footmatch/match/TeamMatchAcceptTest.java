package com.dhoon.footmatch.match;

import com.dhoon.footmatch.match.domain.TeamMatch;
import com.dhoon.footmatch.match.domain.TeamMatchAcceptRequest;
import com.dhoon.footmatch.match.domain.TeamMatchAcceptRequestStatus;
import com.dhoon.footmatch.match.domain.TeamMatchStatus;
import com.dhoon.footmatch.match.dto.response.TeamMatchAcceptRequestResponse;
import com.dhoon.footmatch.match.dto.response.TeamMatchCreateResponse;
import com.dhoon.footmatch.match.dto.response.TeamMatchMatchedResponse;
import com.dhoon.footmatch.match.exception.exceptions.NotPendingTeamMatchException;
import com.dhoon.footmatch.match.repository.TeamMatchAcceptRequestRepository;
import com.dhoon.footmatch.match.repository.TeamMatchRepository;
import com.dhoon.footmatch.match.service.TeamMatchService;
import com.dhoon.footmatch.support.IntegrateTest;
import com.dhoon.footmatch.support.fixture.TeamFixture;
import com.dhoon.footmatch.support.fixture.TeamJoinRequestFixture;
import com.dhoon.footmatch.support.fixture.TeamMatchFixture;
import com.dhoon.footmatch.team.exception.exceptions.NotTeamLeaderException;
import com.dhoon.footmatch.teamjoinrequest.dto.response.TeamJoinRequestAcceptResponse;
import com.dhoon.footmatch.teammember.exception.exceptions.NotTeamMemberException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static com.dhoon.footmatch.support.fixture.TeamFixture.*;
import static org.assertj.core.api.Assertions.*;

@IntegrateTest
public class TeamMatchAcceptTest {

    @Autowired private TeamMatchService teamMatchService;
    @Autowired private TeamMatchRepository teamMatchRepository;
    @Autowired private TeamMatchAcceptRequestRepository teamMatchAcceptRequestRepository;

    @Autowired private TeamFixture teamFixture;
    @Autowired private TeamMatchFixture teamMatchFixture;
    @Autowired private TeamJoinRequestFixture teamJoinRequestFixture;

    public static final LocalDateTime MATCH_PLAYED_AT = LocalDateTime.of(2030, 1, 1, 12, 30);

    @Test
    @DisplayName(value = "자신의 팀 매치에 들어온 수락요청을 수락할 수 있다.")
    void team_match_request_accept() throws Exception {
        // given
        TeamFixtureData teamA = teamFixture.createTeamWithLeaderMember("leaderA", "teamA");
        TeamMatchCreateResponse match = teamMatchFixture.createTeamMatch(teamA.team().getTeamId(), teamA.leader().getMemberId(), MATCH_PLAYED_AT);

        TeamFixtureData teamB = teamFixture.createTeamWithLeaderMember("leaderB", "teamB");
        TeamMatchAcceptRequestResponse request = teamMatchService.requestTeamMatchAcceptance(match.getMatchId(), teamB.leader().getMemberId());

        // when
        teamMatchService.acceptTeamMatchRequest(match.getMatchId(), request.getRequestId(), teamA.leader().getMemberId());
        TeamMatch entityTeamMatch = teamMatchRepository.findById(match.getMatchId()).get();
        TeamMatchAcceptRequest entityRequest = teamMatchAcceptRequestRepository.findById(request.getRequestId()).get();

        // then
        assertThat(entityTeamMatch.getTeamMatchStatus()).isEqualTo(TeamMatchStatus.MATCHED);
        assertThat(entityTeamMatch.getAwayTeam().getId()).isEqualTo(teamB.team().getTeamId());
        assertThat(entityRequest.getTeamMatch().getId()).isEqualTo(entityTeamMatch.getId());
        assertThat(entityRequest.getStatus()).isEqualTo(TeamMatchAcceptRequestStatus.ACCEPTED);
    }

    @Test
    @DisplayName(value = "이미 MATCHED 상태인 매치에는 수락요청을 보낼 수 없다.")
    void team_match_request_accept_fail_MATCHED() throws Exception {
        // given
        TeamFixtureData teamA = teamFixture.createTeamWithLeaderMember("leaderA", "teamA");
        TeamMatchCreateResponse match = teamMatchFixture.createTeamMatch(teamA.team().getTeamId(), teamA.leader().getMemberId(), MATCH_PLAYED_AT);

        TeamFixtureData teamB = teamFixture.createTeamWithLeaderMember("leaderB", "teamB");
        TeamMatchAcceptRequestResponse request = teamMatchService.requestTeamMatchAcceptance(match.getMatchId(), teamB.leader().getMemberId());
        teamMatchService.acceptTeamMatchRequest(match.getMatchId(), request.getRequestId(), teamA.leader().getMemberId()); // match -> MATCHED

        // when && then
        assertThatThrownBy(() -> teamMatchService.requestTeamMatchAcceptance(match.getMatchId(), teamB.leader().getMemberId()))
                .isInstanceOf(NotPendingTeamMatchException.class);
    }

    @Test
    @DisplayName(value = "매치를 등록한 팀 소속이 아니면, 매치를 수락할 수 없다.")
    void team_match_request_accept_fail_not_team_member() throws Exception {
        // given
        TeamFixtureData teamA = teamFixture.createTeamWithLeaderMember("leaderA", "teamA");
        TeamMatchCreateResponse match = teamMatchFixture.createTeamMatch(teamA.team().getTeamId(), teamA.leader().getMemberId(), MATCH_PLAYED_AT);

        TeamFixtureData teamB = teamFixture.createTeamWithLeaderMember("leaderB", "teamB");
        TeamMatchAcceptRequestResponse request = teamMatchService.requestTeamMatchAcceptance(match.getMatchId(), teamB.leader().getMemberId());

        TeamFixtureData teamC = teamFixture.createTeamWithLeaderMember("leaderC", "teamC");

        // when && then
        assertThatThrownBy(() -> teamMatchService.acceptTeamMatchRequest(match.getMatchId(), request.getRequestId(), teamC.leader().getMemberId()))
                .isInstanceOf(NotTeamMemberException.class);
    }

    @Test
    @DisplayName(value = "팀장이 아닌경우 매치를 수락할 수 없다.")
    void team_match_request_accept_fail_not_team_leader() throws Exception {
        // given
        TeamFixtureData teamA = teamFixture.createTeamWithLeaderMember("leaderA", "teamA");
        TeamMatchCreateResponse match = teamMatchFixture.createTeamMatch(teamA.team().getTeamId(), teamA.leader().getMemberId(), MATCH_PLAYED_AT);

        TeamFixtureData teamB = teamFixture.createTeamWithLeaderMember("leaderB", "teamB");
        TeamMatchAcceptRequestResponse request = teamMatchService.requestTeamMatchAcceptance(match.getMatchId(), teamB.leader().getMemberId());

        TeamJoinRequestAcceptResponse join = teamJoinRequestFixture.requestCreateAndAccept(teamA.team().getTeamId(), "user");

        // when && then
        assertThatThrownBy(() -> teamMatchService.acceptTeamMatchRequest(match.getMatchId(), request.getRequestId(), join.getMemberId()))
                .isInstanceOf(NotTeamLeaderException.class);
    }

}
