package com.dhoon.footmatch.match.controller;

import com.dhoon.footmatch.match.dto.request.TeamMatchCreateRequest;
import com.dhoon.footmatch.match.dto.response.TeamMatchCreateResponse;
import com.dhoon.footmatch.match.service.TeamMatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class TeamMatchController {

    private final TeamMatchService teamMatchService;

    @PostMapping("/api/team-match/{teamId}")
    public ResponseEntity<TeamMatchCreateResponse> createTeamMatch(@PathVariable Long teamId, @AuthenticationPrincipal Jwt jwt, @Valid @RequestBody TeamMatchCreateRequest request) {
        TeamMatchCreateResponse response = teamMatchService.createTeamMatch(teamId, Long.valueOf(jwt.getSubject()), request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
