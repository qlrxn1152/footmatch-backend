package com.dhoon.footmatch.member.dto.response;

import com.dhoon.footmatch.member.domain.Member;
import com.dhoon.footmatch.member.domain.MemberRole;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberMeResponse {

    private Long memberId;
    private String username;
    private MemberRole role;
    private int rating;
    private int totalGoalCount;
    private LocalDateTime createdAt;

    public static MemberMeResponse of(Member member) {
        return new MemberMeResponse(
                member.getId(),
                member.getUsername(),
                member.getRole(),
                member.getRating(),
                member.getTotalGoalCount(),
                member.getCreatedAt()
        );
    }
}
