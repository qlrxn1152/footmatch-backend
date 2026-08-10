package com.dhoon.footmatch.auth.service.impl;

import com.dhoon.footmatch.auth.dto.request.MemberLoginRequest;
import com.dhoon.footmatch.auth.dto.response.MemberLoginResponse;
import com.dhoon.footmatch.auth.exception.exceptions.InvalidLoginPasswordException;
import com.dhoon.footmatch.auth.exception.exceptions.InvalidLoginUsernameException;
import com.dhoon.footmatch.auth.jwt.AccessToken;
import com.dhoon.footmatch.auth.jwt.JwtTokenProvider;
import com.dhoon.footmatch.auth.service.AuthService;
import com.dhoon.footmatch.member.domain.Member;
import com.dhoon.footmatch.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class AuthServiceImpl implements AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional(readOnly = true)
    public MemberLoginResponse login(MemberLoginRequest request) {
        Member member = memberRepository.findByUsername(request.getUsername())
                .orElseThrow(InvalidLoginUsernameException::new);

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new InvalidLoginPasswordException();
        }

        // 로그인 시킴 -> 서버가, 유저에게 JWT 토큰을 발급한다.
        AccessToken accessToken = jwtTokenProvider.createAccessToken(member);

        return MemberLoginResponse.of(member, accessToken);
    }

}
