package com.dhoon.footmatch.match.dto.request;

import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TeamMatchResultCreateRequest {

    @Min(0)
    private int homeScore;

    @Min(0)
    private int awayScore;

    public static TeamMatchResultCreateRequest of(int homeScore, int awayScore) {
        return new TeamMatchResultCreateRequest(homeScore, awayScore);
    }
}
