package com.dhoon.footmatch.team.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamListResponse {

    private List<TeamListItemResponse> teams = new ArrayList<>();

    public static TeamListResponse of(List<TeamListItemResponse> teams) {
        return new TeamListResponse(teams);
    }
}
