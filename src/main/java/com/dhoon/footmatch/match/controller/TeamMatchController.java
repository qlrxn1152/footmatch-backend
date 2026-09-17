package com.dhoon.footmatch.match.controller;

import com.dhoon.footmatch.match.dto.request.TeamMatchCreateRequest;
import com.dhoon.footmatch.match.dto.request.TeamMatchResultCreateRequest;
import com.dhoon.footmatch.match.dto.response.*;
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

    @PostMapping("/api/team-matches/{teamId}/matches")
    public ResponseEntity<TeamMatchCreateResponse> createTeamMatch(@PathVariable Long teamId, @AuthenticationPrincipal Jwt jwt, @Valid @RequestBody TeamMatchCreateRequest request) {
        TeamMatchCreateResponse response = teamMatchService.createTeamMatch(teamId, Long.valueOf(jwt.getSubject()), request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/api/team-matches/{matchId}/accept-requests")
    public ResponseEntity<TeamMatchAcceptRequestResponse> requestTeamMatchAcceptance(@PathVariable Long matchId, @AuthenticationPrincipal Jwt jwt) {
        TeamMatchAcceptRequestResponse response = teamMatchService.requestTeamMatchAcceptance(matchId, Long.valueOf(jwt.getSubject()));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping("/api/team-matches/{teamId}/request/pendings")
    public ResponseEntity<TeamMatchAcceptRequestsResponse> getTeamPendingMatchAcceptRequests(@PathVariable Long teamId, @AuthenticationPrincipal Jwt jwt) {
        TeamMatchAcceptRequestsResponse response = teamMatchService.getTeamMatchAcceptRequests(teamId, Long.valueOf(jwt.getSubject()));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/api/team-matches/{matchId}/accept-requests/{requestId}")
    public ResponseEntity<TeamMatchMatchedResponse> acceptTeamMatch(@PathVariable Long matchId, @PathVariable Long requestId, @AuthenticationPrincipal Jwt jwt) {
        TeamMatchMatchedResponse response = teamMatchService.acceptTeamMatchRequest(matchId, requestId, Long.valueOf(jwt.getSubject()));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }



    // 쿼리파라미터 -> dto 를 다양하게 못보내고 통일된 ... MATCHED , COMPLETED, PENDING -> DTO 에 필요한값들이 각각 다 다름 .. => 쿼리파라미터를 사용 X
    @GetMapping("/api/teams/{teamId}/matches/pending")
    public ResponseEntity<TeamPendingMatchesResponse> getTeamPendingMatches(@PathVariable Long teamId) {
        TeamPendingMatchesResponse response = teamMatchService.getTeamPendingMatches(teamId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/api/teams/{teamId}/matches/matched")
    public ResponseEntity<TeamMatchedMatchesResponse> getTeamMatchedMatches(@PathVariable Long teamId) {
        TeamMatchedMatchesResponse response = teamMatchService.getTeamMatchedMatches(teamId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/api/teams/{teamId}/matches/completed")
    public ResponseEntity<TeamCompletedMatchesResponse> getTeamCompletedMatches(@PathVariable Long teamId) {
        TeamCompletedMatchesResponse response = teamMatchService.getTeamCompletedMatches(teamId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }



    // 모든팀의 PENDING 매치들 조회
    @GetMapping("/api/team-matches/pending")
    public ResponseEntity<TeamMatchPendingListResponse> getPendingMatches() {
        TeamMatchPendingListResponse response = teamMatchService.getPendingMatches();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 모든팀의 MATCHED 매치들 조회
    @GetMapping("/api/team-matches/matched")
    public ResponseEntity<TeamMatchedMatchesResponse> getMatchedMatches() {
        TeamMatchedMatchesResponse response = teamMatchService.getMatchedMatches();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 모든팀의 COMPLETED 매치들 조회
    @GetMapping("/api/team-matches/completed")
    public ResponseEntity<TeamCompletedMatchesResponse> getCompletedMatches() {
        TeamCompletedMatchesResponse response = teamMatchService.getCompletedMatches();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }



    
    @PostMapping("/api/team-matches/{matchId}/result")
    public ResponseEntity<TeamMatchResultCreateResponse> createTeamMatchResult(@PathVariable Long matchId, @Valid @RequestBody TeamMatchResultCreateRequest request) {
        TeamMatchResultCreateResponse response = teamMatchService.createTeamMatchResult(matchId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


}
