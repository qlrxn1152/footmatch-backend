package com.dhoon.footmatch.teamjoinrequest.controller;

import com.dhoon.footmatch.teamjoinrequest.dto.response.TeamJoinRequestAcceptResponse;
import com.dhoon.footmatch.teamjoinrequest.dto.response.TeamJoinRequestResponse;
import com.dhoon.footmatch.teamjoinrequest.service.TeamJoinRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class TeamJoinRequestController {

    private final TeamJoinRequestService teamJoinRequestService;

    @PostMapping("/api/teams/{teamId}/join-request")
    public ResponseEntity<TeamJoinRequestResponse> joinRequest(@PathVariable Long teamId, @AuthenticationPrincipal Jwt jwt) {
        TeamJoinRequestResponse response = teamJoinRequestService.joinRequest(teamId, Long.valueOf(jwt.getSubject()));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/api/teams/{teamId}/join-request/{requestId}/accept")
    public ResponseEntity<TeamJoinRequestAcceptResponse> acceptRequest(@PathVariable Long teamId, @PathVariable Long requestId, @AuthenticationPrincipal Jwt jwt) {
        TeamJoinRequestAcceptResponse response = teamJoinRequestService.acceptRequest(teamId, requestId, Long.valueOf(jwt.getSubject()));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
