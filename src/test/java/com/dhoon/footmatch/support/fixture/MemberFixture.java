package com.dhoon.footmatch.support.fixture;

import com.dhoon.footmatch.member.dto.request.MemberCreateRequest;
import com.dhoon.footmatch.member.dto.response.MemberCreateResponse;
import com.dhoon.footmatch.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberFixture {

    private final MemberService memberService;

    public MemberCreateResponse signupMember(String username, String password) {
        return memberService.signup(new MemberCreateRequest(username, password));
    }

}
