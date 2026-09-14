package com.dhoon.footmatch.match;

import com.dhoon.footmatch.match.domain.TeamMatch;
import com.dhoon.footmatch.match.domain.TeamMatchStatus;
import com.dhoon.footmatch.match.dto.response.TeamMatchCreateResponse;
import com.dhoon.footmatch.match.dto.response.TeamMatchPendingListResponse;
import com.dhoon.footmatch.match.dto.response.TeamMatchPendingResponse;
import com.dhoon.footmatch.match.repository.TeamMatchRepository;
import com.dhoon.footmatch.match.service.TeamMatchService;
import com.dhoon.footmatch.support.IntegrateTest;
import com.dhoon.footmatch.support.fixture.TeamFixture;
import com.dhoon.footmatch.support.fixture.TeamMatchFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static com.dhoon.footmatch.support.fixture.TeamFixture.*;
import static org.assertj.core.api.Assertions.*;

@IntegrateTest
public class TeamMatchQueryTest {

    @Autowired private TeamMatchService teamMatchService;
    @Autowired private TeamMatchRepository teamMatchRepository;

    @Autowired private TeamFixture teamFixture;
    @Autowired private TeamMatchFixture teamMatchFixture;

    public static final LocalDateTime MATCH_PLAYED_AT = LocalDateTime.of(2030, 1, 1, 12, 30);

    @Test
    @DisplayName(value = "등록되어져 있는 PENDING 매치들을 조회할 수 있다.")
    void getPendingMatches() throws Exception {
        // given
        TeamFixtureData dataA = teamFixture.createTeamWithLeaderMember("leaderA", "teamA");
        TeamMatchCreateResponse teamAMatch = teamMatchFixture.createTeamMatch(dataA.team().getTeamId(), dataA.leader().getMemberId(), MATCH_PLAYED_AT);

        TeamFixtureData dataB = teamFixture.createTeamWithLeaderMember("leaderB", "teamB");
        TeamMatchCreateResponse teamBMatch = teamMatchFixture.createTeamMatch(dataB.team().getTeamId(), dataB.leader().getMemberId(), MATCH_PLAYED_AT.plusDays(20));

        TeamFixtureData dataC = teamFixture.createTeamWithLeaderMember("leaderC", "teamC");
        TeamMatchCreateResponse teamCMatch = teamMatchFixture.createTeamMatch(dataC.team().getTeamId(), dataC.leader().getMemberId(), MATCH_PLAYED_AT.plusDays(2));

        // when
        TeamMatchPendingListResponse response = teamMatchService.getPendingMatches();
        List<TeamMatch> allMatches = teamMatchRepository.findAll();

        // then
        assertThat(response.getPendingMatches()).hasSize(3);
        assertThat(response.getPendingMatches()).extracting(TeamMatchPendingResponse::getHomeTeamName).containsExactly("teamA", "teamB", "teamC");
        assertThat(allMatches).extracting(TeamMatch::getTeamMatchStatus).containsExactly(TeamMatchStatus.PENDING,  TeamMatchStatus.PENDING, TeamMatchStatus.PENDING);
        assertThat(allMatches).extracting(TeamMatch::getId).containsExactly(teamAMatch.getMatchId(), teamBMatch.getMatchId(), teamCMatch.getMatchId());
    }


}
