package com.dhoon.footmatch.team.dto.request;

import com.dhoon.footmatch.team.dto.response.TeamNameChangeResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamNameChangeRequest {

    @NotBlank
    @Size(min = 2, max = 20)
    private String teamName;

    public static TeamNameChangeRequest of(String teamName) {
        return new TeamNameChangeRequest(teamName);
    }

}
