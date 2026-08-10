package com.dhoon.footmatch.member.service.impl;

import com.dhoon.footmatch.member.domain.Member;
import com.dhoon.footmatch.member.dto.request.MemberCreateRequest;
import com.dhoon.footmatch.member.dto.response.MemberCreateResponse;
import com.dhoon.footmatch.member.dto.response.MemberMeResponse;
import com.dhoon.footmatch.member.repository.MemberRepository;
import com.dhoon.footmatch.member.service.MemberService;
import com.dhoon.footmatch.member.validation.MemberValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    private final MemberValidator memberValidator;


    @Override
    public MemberCreateResponse signup(MemberCreateRequest request) {
        memberValidator.validateDuplicateUsername(request.getUsername());
        memberValidator.validateUsernameContainStrip(request.getUsername());

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        Member savedMember = memberRepository.save(Member.signup(request.getUsername(), encodedPassword));
        return MemberCreateResponse.of(savedMember);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberMeResponse getMe(Long memberId) {
        Member member = memberValidator.validateExistMemberAndReturn(memberId);
        return MemberMeResponse.of(member);
    }
}
