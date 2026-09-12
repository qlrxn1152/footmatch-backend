package com.dhoon.footmatch.match;

import com.dhoon.footmatch.match.domain.TeamMatch;
import com.dhoon.footmatch.match.domain.TeamMatchStatus;
import com.dhoon.footmatch.match.dto.request.TeamMatchCreateRequest;
import com.dhoon.footmatch.match.dto.response.TeamMatchCreateResponse;
import com.dhoon.footmatch.match.exception.exceptions.AlreadyExistPendingMatchException;
import com.dhoon.footmatch.match.exception.exceptions.InvalidMatchPlayedAtException;
import com.dhoon.footmatch.match.repository.TeamMatchRepository;
import com.dhoon.footmatch.match.service.TeamMatchService;
import com.dhoon.footmatch.support.IntegrateTest;
import com.dhoon.footmatch.support.fixture.TeamFixture;
import com.dhoon.footmatch.support.fixture.TeamJoinRequestFixture;
import com.dhoon.footmatch.team.exception.exceptions.NotTeamLeaderException;
import com.dhoon.footmatch.teamjoinrequest.dto.response.TeamJoinRequestAcceptResponse;
import com.dhoon.footmatch.teammember.exception.exceptions.NotTeamMemberException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDateTime;

import static com.dhoon.footmatch.support.fixture.TeamFixture.*;
import static org.assertj.core.api.Assertions.*;

@IntegrateTest
public class TeamMatchCreateTest {

    @Autowired private TeamMatchService teamMatchService;
    @Autowired private TeamMatchRepository teamMatchRepository;

    @Autowired private TeamFixture teamFixture;
    @Autowired private TeamJoinRequestFixture teamJoinRequestFixture;

    private static final LocalDateTime MATCH_PLAY_AT = LocalDateTime.of(2030, 1, 1, 12, 00); // 2030 - 01 - 01 12:00 매치일

    private static TeamMatchCreateRequest getTeamMatchCreateRequest(LocalDateTime playedAt) {
        return new TeamMatchCreateRequest(playedAt);
    }

    @Test
    @DisplayName(value = "팀장은 팀 매치를 생성할 수 있다.")
    void createTeamMatch() throws Exception {
        // given
        TeamFixtureData fixture = teamFixture.createTeamWithLeaderMember("leaderA", "teamA");

        // when
        TeamMatchCreateResponse response = teamMatchService.createTeamMatch(fixture.team().getTeamId(), fixture.leader().getMemberId(), getTeamMatchCreateRequest(MATCH_PLAY_AT));
        TeamMatch savedTeamMatch = teamMatchRepository.findById(response.getMatchId()).get();

        // then
        assertThat(savedTeamMatch.getId()).isEqualTo(response.getMatchId());
        assertThat(savedTeamMatch.getAwayTeam()).isNull();
        assertThat(savedTeamMatch.getHomeTeam().getId()).isEqualTo(fixture.team().getTeamId());
        assertThat(savedTeamMatch.getCreatedAt()).isNotSameAs(savedTeamMatch.getPlayedAt());
        assertThat(savedTeamMatch.getPlayedAt()).isEqualTo(MATCH_PLAY_AT);
        assertThat(savedTeamMatch.getTeamMatchStatus()).isEqualTo(TeamMatchStatus.PENDING);
    }

    @Test
    @DisplayName(value = "팀에 가입되어져있는 일반 회원은 매치를 생성할 수 없다.")
    void createTeamMatch_fail_notTeamLeader() throws Exception {
        // given
        TeamFixtureData fixture = teamFixture.createTeamWithLeaderMember("leaderA", "teamA");
        TeamJoinRequestAcceptResponse user = teamJoinRequestFixture.requestCreateAndAccept(fixture.team().getTeamId(), "userA");

        // when && then
        assertThatThrownBy(() -> teamMatchService.createTeamMatch(fixture.team().getTeamId(), user.getMemberId(), getTeamMatchCreateRequest(MATCH_PLAY_AT)))
                .isInstanceOf(NotTeamLeaderException.class);
        boolean isCreated = teamMatchRepository.existsByHomeTeamIdAndTeamMatchStatus(fixture.team().getTeamId(), TeamMatchStatus.PENDING);

        assertThat(isCreated).isFalse();
    }

    @Test
    @DisplayName(value = "다른팀 팀장은 매치를 생성할 수 없다.")
    void createTeamMatch_fail_not_sameTeam() throws Exception {
        // given
        TeamFixtureData fixture = teamFixture.createTeamWithLeaderMember("leaderA", "teamA");
        TeamFixtureData fixture2 = teamFixture.createTeamWithLeaderMember("leaderB", "teamB");

        // when && then
        assertThatThrownBy(() -> teamMatchService.createTeamMatch(fixture.team().getTeamId(), fixture2.leader().getMemberId(), getTeamMatchCreateRequest(MATCH_PLAY_AT)))
                .isInstanceOf(NotTeamMemberException.class);

        boolean isCreated = teamMatchRepository.existsByHomeTeamIdAndTeamMatchStatus(fixture.team().getTeamId(), TeamMatchStatus.PENDING);

        assertThat(isCreated).isFalse();
    }

    @Test
    @DisplayName(value = "playedAt 이 null 일 경우, 매치를 생성할 수 없다.")
    void createTeamMatch_fail_playedAt_null() throws Exception {
        // given
        TeamFixtureData fixture = teamFixture.createTeamWithLeaderMember("leaderA", "teamA");

        // when && then
        assertThatThrownBy(() -> teamMatchService.createTeamMatch(fixture.team().getTeamId(), fixture.leader().getMemberId(), getTeamMatchCreateRequest(null)))
                .isInstanceOf(InvalidMatchPlayedAtException.class);
    }

    @Test
    @DisplayName(value = "playedAt 이 현재 시간보다 과거일경우, 매치를 생성할 수 없다.")
    void createTeamMatch_fail_playedAt_not_future() throws Exception {
        // given
        TeamFixtureData fixture = teamFixture.createTeamWithLeaderMember("leaderA", "teamA");

        // when && then
        assertThatThrownBy(() -> teamMatchService.createTeamMatch(fixture.team().getTeamId(), fixture.leader().getMemberId(), getTeamMatchCreateRequest(LocalDateTime.of(1111, 1, 1, 12, 00))))
                .isInstanceOf(InvalidMatchPlayedAtException.class);
    }

    @Test
    @DisplayName(value = "playedAt 이 현재 시간보다 과거일경우, 매치를 생성할 수 없다.")
    void createTeamMatch_fail_already_pendingMatch() throws Exception {
        // given
        TeamFixtureData fixture = teamFixture.createTeamWithLeaderMember("leaderA", "teamA");
        teamMatchService.createTeamMatch(fixture.team().getTeamId(), fixture.leader().getMemberId(), getTeamMatchCreateRequest(MATCH_PLAY_AT));

        // when && then
        assertThatThrownBy(() -> teamMatchService.createTeamMatch(fixture.team().getTeamId(), fixture.leader().getMemberId(), getTeamMatchCreateRequest(MATCH_PLAY_AT.plusDays(1))))
                .isInstanceOf(AlreadyExistPendingMatchException.class);
    }



}
