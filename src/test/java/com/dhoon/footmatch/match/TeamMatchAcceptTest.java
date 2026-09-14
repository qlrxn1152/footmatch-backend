package com.dhoon.footmatch.match;

import com.dhoon.footmatch.match.repository.TeamMatchAcceptRequestRepository;
import com.dhoon.footmatch.match.repository.TeamMatchRepository;
import com.dhoon.footmatch.match.service.TeamMatchService;
import com.dhoon.footmatch.support.IntegrateTest;
import com.dhoon.footmatch.support.fixture.TeamFixture;
import com.dhoon.footmatch.support.fixture.TeamMatchFixture;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

@IntegrateTest
public class TeamMatchAcceptTest {

    @Autowired private TeamMatchService teamMatchService;
    @Autowired private TeamMatchRepository teamMatchRepository;
    @Autowired private TeamMatchAcceptRequestRepository teamMatchAcceptRequestRepository;

    @Autowired private TeamFixture teamFixture;
    @Autowired private TeamMatchFixture teamMatchFixture;

    public static final LocalDateTime MATCH_PLAYED_AT = LocalDateTime.of(2030, 1, 1, 12, 30);





}
