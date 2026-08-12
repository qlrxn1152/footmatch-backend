package com.dhoon.footmatch.team;

import com.dhoon.footmatch.support.IntegrateTest;
import com.dhoon.footmatch.support.fixture.TeamFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrateTest
public class TeamLeaderTransferTest {

    @Autowired private TeamFixture teamFixture;

    @Test
    @DisplayName(value = "현재 팀장은 새로운 팀원에게 팀장권한을 위임할 수 있다.")
    void transferLeader() throws Exception {
        // given


        // when

        // then

    }



}
