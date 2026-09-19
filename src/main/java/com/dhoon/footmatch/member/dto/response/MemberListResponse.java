package com.dhoon.footmatch.member.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberListResponse {

    private List<MemberListItemResponse> members = new ArrayList<>();

    public static MemberListResponse of(List<MemberListItemResponse> members) {
        return new MemberListResponse(members);
    }

}
