package com.dhoon.footmatch.member.service;

import com.dhoon.footmatch.member.dto.request.MemberCreateRequest;
import com.dhoon.footmatch.member.dto.response.MemberCreateResponse;
import com.dhoon.footmatch.member.dto.response.MemberMeResponse;

public interface MemberService {

    MemberCreateResponse signup(MemberCreateRequest request);

    MemberMeResponse getMe(Long memberId);
}
