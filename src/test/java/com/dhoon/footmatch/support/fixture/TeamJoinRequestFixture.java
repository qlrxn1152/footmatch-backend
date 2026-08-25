package com.dhoon.footmatch.support.fixture;

import com.dhoon.footmatch.member.domain.Member;
import com.dhoon.footmatch.member.dto.response.MemberCreateResponse;
import com.dhoon.footmatch.team.domain.Team;
import com.dhoon.footmatch.team.exception.exceptions.NotFoundTeamException;
import com.dhoon.footmatch.team.repository.TeamRepository;
import com.dhoon.footmatch.teamjoinrequest.domain.TeamJoinRequest;
import com.dhoon.footmatch.teamjoinrequest.dto.response.TeamJoinRequestAcceptResponse;
import com.dhoon.footmatch.teamjoinrequest.dto.response.TeamJoinRequestResponse;
import com.dhoon.footmatch.teamjoinrequest.service.TeamJoinRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.dhoon.footmatch.support.fixture.TeamFixture.*;

@Component
@RequiredArgsConstructor
public class TeamJoinRequestFixture {

    private final TeamJoinRequestService teamJoinRequestService;

    private final TeamFixture teamFixture;
    private final MemberFixture memberFixture;

    private final TeamRepository teamRepository;

    public TeamJoinRequestResponse createTeamJoinRequest(String leaderUsername, String memberUsername, String teamName) {
        TeamFixtureData teamMember = teamFixture.createTeamWithLeaderMember(leaderUsername, teamName);
        MemberCreateResponse member = memberFixture.signupMember(memberUsername, "1234");

        return teamJoinRequestService.joinRequest(teamMember.team().getTeamId(), member.getMemberId());
    }

    public TeamJoinRequestResponse createTeamJoinRequest(Long teamId, String requesterUsername) {
        MemberCreateResponse requester = memberFixture.signupMember(requesterUsername, "1234");

        return teamJoinRequestService.joinRequest(teamId, requester.getMemberId());
    }

    public TeamJoinRequestAcceptResponse requestCreateAndAccept(Long teamId) {
        TeamJoinRequestResponse request = createTeamJoinRequest(teamId, "requester"); // 가입요청 생성

        Team team = teamRepository.findById(teamId).orElseThrow(NotFoundTeamException::new);

        return teamJoinRequestService.acceptRequest(request.getTeamId(), request.getJoinRequestId(), team.getLeaderMember().getId()); // 가입요청 수락
    }


}
