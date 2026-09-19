package com.dhoon.footmatch.match.dto.request;

import com.dhoon.footmatch.match.domain.TeamMatchScorer;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TeamMatchResultScorersRequest {

    private List<TeamMatchScorer> scorers = new ArrayList<>();


}
