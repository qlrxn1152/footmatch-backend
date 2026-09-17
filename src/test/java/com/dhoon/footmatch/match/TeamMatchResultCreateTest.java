package com.dhoon.footmatch.match;

import com.dhoon.footmatch.match.domain.TeamMatch;
import com.dhoon.footmatch.match.domain.TeamMatchResult;
import com.dhoon.footmatch.match.domain.TeamMatchStatus;
import com.dhoon.footmatch.match.dto.request.TeamMatchResultCreateRequest;
import com.dhoon.footmatch.match.dto.response.TeamMatchMatchedResponse;
import com.dhoon.footmatch.match.dto.response.TeamMatchResultCreateResponse;
import com.dhoon.footmatch.match.repository.TeamMatchAcceptRequestRepository;
import com.dhoon.footmatch.match.repository.TeamMatchRepository;
import com.dhoon.footmatch.match.repository.TeamMatchResultRepository;
import com.dhoon.footmatch.match.service.TeamMatchService;
import com.dhoon.footmatch.support.IntegrateTest;
import com.dhoon.footmatch.support.fixture.TeamFixture;
import com.dhoon.footmatch.support.fixture.TeamJoinRequestFixture;
import com.dhoon.footmatch.support.fixture.TeamMatchFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static com.dhoon.footmatch.support.fixture.TeamFixture.*;
import static org.assertj.core.api.Assertions.*;

@IntegrateTest
public class TeamMatchResultCreateTest {

    @Autowired private TeamMatchService teamMatchService;
    @Autowired private TeamMatchResultRepository teamMatchResultRepository;
    @Autowired private TeamMatchRepository teamMatchRepository;

    @Autowired private TeamFixture teamFixture;
    @Autowired private TeamMatchFixture teamMatchFixture;

    public static final LocalDateTime MATCH_PLAYED_AT = LocalDateTime.of(2030, 1, 1, 12, 30);

    public static TeamMatchResultCreateRequest createTeamMatchResultCreateRequest(int homeScore, int awayScore) {
        return new TeamMatchResultCreateRequest(homeScore, awayScore);
    }

    @Test
    void 홈팀이_더_많은_득점을_하면_승리팀은_홈팀이_된다() throws Exception {
        // given
        TeamFixtureData teamA = teamFixture.createTeamWithLeaderMember("leaderA", "teamA");
        TeamFixtureData teamB = teamFixture.createTeamWithLeaderMember("leaderB", "teamB");

        // teamA 매치 생성 -> teamB 요청 -> teamA 팀장이 요청을 수락 -> MATCHED
        TeamMatchMatchedResponse match = teamMatchFixture.createMatchAndMatched(teamA.team().getTeamId(), teamA.leader().getMemberId(), MATCH_PLAYED_AT, teamB.leader().getMemberId());


        // when
        TeamMatchResultCreateResponse response = teamMatchService.createTeamMatchResult(match.getMatchId(), createTeamMatchResultCreateRequest(3, 1));// 3:1 홈팀승리

        TeamMatchResult entityMatchResult = teamMatchResultRepository.findById(response.getMatchResultId()).get();
        TeamMatch entityTeamMatch = teamMatchRepository.findById(response.getMatchId()).get();

        // then
        assertThat(response.getWinnerTeamName()).isEqualTo("teamA");
        assertThat(response.getMatchId()).isEqualTo(match.getMatchId());

        assertThat(entityMatchResult.getTeamMatch().getId()).isEqualTo(match.getMatchId());
        assertThat(entityMatchResult.getHomeScore()).isEqualTo(3);
        assertThat(entityMatchResult.getAwayScore()).isEqualTo(1);
        assertThat(entityMatchResult.getWinnerTeam().getId()).isEqualTo(teamA.team().getTeamId());

        assertThat(entityTeamMatch.getTeamMatchStatus()).isEqualTo(TeamMatchStatus.COMPLETED);
    }

    @Test
    void 원정팀이_더_많은_득점을_하면_승리팀은_원정팀이_된다() throws Exception {
        // given
        TeamFixtureData teamA = teamFixture.createTeamWithLeaderMember("leaderA", "teamA");
        TeamFixtureData teamB = teamFixture.createTeamWithLeaderMember("leaderB", "teamB");

        // teamA 매치 생성 -> teamB 요청 -> teamA 팀장이 요청을 수락 -> MATCHED
        TeamMatchMatchedResponse match = teamMatchFixture.createMatchAndMatched(teamA.team().getTeamId(), teamA.leader().getMemberId(), MATCH_PLAYED_AT, teamB.leader().getMemberId());

        // when
        TeamMatchResultCreateResponse response = teamMatchService.createTeamMatchResult(match.getMatchId(), createTeamMatchResultCreateRequest(2, 4));// 2:4 원정팀승리

        TeamMatchResult entityMatchResult = teamMatchResultRepository.findById(response.getMatchResultId()).get();
        TeamMatch entityTeamMatch = teamMatchRepository.findById(response.getMatchId()).get();

        // then
        assertThat(response.getWinnerTeamName()).isEqualTo("teamB");
        assertThat(response.getMatchId()).isEqualTo(match.getMatchId());

        assertThat(entityMatchResult.getTeamMatch().getId()).isEqualTo(match.getMatchId());
        assertThat(entityMatchResult.getHomeScore()).isEqualTo(2);
        assertThat(entityMatchResult.getAwayScore()).isEqualTo(4);
        assertThat(entityMatchResult.getWinnerTeam().getId()).isEqualTo(teamB.team().getTeamId());

        assertThat(entityTeamMatch.getTeamMatchStatus()).isEqualTo(TeamMatchStatus.COMPLETED);
    }

    @Test
    void 무승부인경우_winnerTeam_is_Null() throws Exception {
        // given
        TeamFixtureData teamA = teamFixture.createTeamWithLeaderMember("leaderA", "teamA");
        TeamFixtureData teamB = teamFixture.createTeamWithLeaderMember("leaderB", "teamB");

        // teamA 매치 생성 -> teamB 요청 -> teamA 팀장이 요청을 수락 -> MATCHED
        TeamMatchMatchedResponse match = teamMatchFixture.createMatchAndMatched(teamA.team().getTeamId(), teamA.leader().getMemberId(), MATCH_PLAYED_AT, teamB.leader().getMemberId());

        // when
        TeamMatchResultCreateResponse response = teamMatchService.createTeamMatchResult(match.getMatchId(), createTeamMatchResultCreateRequest(2, 2)); // 2:2 무승부

        TeamMatchResult entityMatchResult = teamMatchResultRepository.findById(response.getMatchResultId()).get();
        TeamMatch entityTeamMatch = teamMatchRepository.findById(response.getMatchId()).get();

        // then
        assertThat(response.getWinnerTeamName()).isNull();
        assertThat(response.getMatchId()).isEqualTo(match.getMatchId());

        assertThat(entityMatchResult.getTeamMatch().getId()).isEqualTo(match.getMatchId());
        assertThat(entityMatchResult.getHomeScore()).isEqualTo(2);
        assertThat(entityMatchResult.getAwayScore()).isEqualTo(2);

        assertThat(entityMatchResult.getWinnerTeam()).isNull();

        assertThat(entityTeamMatch.getTeamMatchStatus()).isEqualTo(TeamMatchStatus.COMPLETED);
    }


}
