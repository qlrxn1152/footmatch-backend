package com.dhoon.footmatch.member.dto.response;

import com.dhoon.footmatch.member.domain.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberListItemResponse {

    private Long id;
    private String username;
    private int rating;

    public static MemberListItemResponse of(Member member) {
        return new MemberListItemResponse(
                member.getId(),
                member.getUsername(),
                member.getRating()
        );
    }

}
