package com.dhoon.footmatch.match.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamMatchedMatchesResponse {

    List<TeamMatchedMatchResponse> matchedMatches = new ArrayList<>();

    public static TeamMatchedMatchesResponse of(List<TeamMatchedMatchResponse> matchedMatches) {
        return new TeamMatchedMatchesResponse(matchedMatches);
    }
}
