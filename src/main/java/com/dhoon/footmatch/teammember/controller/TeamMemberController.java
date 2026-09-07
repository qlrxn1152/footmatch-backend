package com.dhoon.footmatch.teammember.controller;

import com.dhoon.footmatch.teammember.dto.response.TeamLeaveResponse;
import com.dhoon.footmatch.teammember.dto.response.TeamMembersDto;
import com.dhoon.footmatch.teammember.service.TeamMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class TeamMemberController {

    private final TeamMemberService teamMemberService;

    @DeleteMapping("/api/teams/{teamId}/members/me")
    public ResponseEntity<TeamLeaveResponse> leaveTeam(@PathVariable Long teamId, @AuthenticationPrincipal Jwt jwt) {
        TeamLeaveResponse response = teamMemberService.leaveTeam(teamId, Long.valueOf(jwt.getSubject()));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/api/teams/{teamId}/members/{targeterId}")
    public ResponseEntity<Void> kickMember(@PathVariable Long teamId, @AuthenticationPrincipal Jwt jwt, @PathVariable Long targeterId) {
        teamMemberService.kickMember(teamId, Long.valueOf(jwt.getSubject()), targeterId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/api/teams/{teamId}/members")
    public ResponseEntity<TeamMembersDto> getTeamMembers(@PathVariable Long teamId) {
        TeamMembersDto response = teamMemberService.getTeamMembers(teamId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
