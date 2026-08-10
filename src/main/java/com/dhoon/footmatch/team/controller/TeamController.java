package com.dhoon.footmatch.team.controller;

import com.dhoon.footmatch.team.dto.request.TeamCreateRequest;
import com.dhoon.footmatch.team.dto.response.TeamCreateResponse;
import com.dhoon.footmatch.team.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @PostMapping("/api/teams")
    public ResponseEntity<TeamCreateResponse> createTeam(@Valid @RequestBody TeamCreateRequest request, @AuthenticationPrincipal Jwt jwt) {
        teamService.createTeam(request);

    }
}
