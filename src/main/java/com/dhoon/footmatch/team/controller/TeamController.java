package com.dhoon.footmatch.team.controller;

import com.dhoon.footmatch.team.dto.request.TeamCreateRequest;
import com.dhoon.footmatch.team.dto.request.TeamLeaderTransferRequest;
import com.dhoon.footmatch.team.dto.request.TeamNameChangeRequest;
import com.dhoon.footmatch.team.dto.response.TeamCreateResponse;
import com.dhoon.footmatch.team.dto.response.TeamDetailResponse;
import com.dhoon.footmatch.team.dto.response.TeamLeaderTransferResponse;
import com.dhoon.footmatch.team.dto.response.TeamNameChangeResponse;
import com.dhoon.footmatch.team.service.TeamService;
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
public class TeamController {

    private final TeamService teamService;

    @PostMapping("/api/teams")
    public ResponseEntity<TeamCreateResponse> createTeam(@Valid @RequestBody TeamCreateRequest request, @AuthenticationPrincipal Jwt jwt) {
        TeamCreateResponse response = teamService.createTeam(request, Long.valueOf(jwt.getSubject()));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/api/teams/{teamId}/name")
    public ResponseEntity<TeamNameChangeResponse> changeTeamName(@Valid @RequestBody TeamNameChangeRequest request, @AuthenticationPrincipal Jwt jwt, @PathVariable Long teamId) {
        TeamNameChangeResponse response = teamService.changeTeamName(request, Long.valueOf(jwt.getSubject()), teamId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/api/teams/{teamId}")
    public ResponseEntity<TeamDetailResponse> getTeam(@PathVariable Long teamId) {
        TeamDetailResponse response = teamService.getTeam(teamId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/api/teams/{teamId}/leader")
    public ResponseEntity<TeamLeaderTransferResponse> transferLeader(@PathVariable Long teamId, @AuthenticationPrincipal Jwt jwt, @Valid @RequestBody TeamLeaderTransferRequest request) {
        TeamLeaderTransferResponse response = teamService.transferLeader(teamId, Long.valueOf(jwt.getSubject()), request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }



}
