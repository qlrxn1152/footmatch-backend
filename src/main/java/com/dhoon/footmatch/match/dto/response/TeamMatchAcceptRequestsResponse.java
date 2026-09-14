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
public class TeamMatchAcceptRequestsResponse {

    private List<TeamMatchAcceptRequestListItemResponse> requests = new ArrayList<>();


    public static TeamMatchAcceptRequestsResponse of(List<TeamMatchAcceptRequestListItemResponse> requests) {
        return new TeamMatchAcceptRequestsResponse(requests);
    }


}
