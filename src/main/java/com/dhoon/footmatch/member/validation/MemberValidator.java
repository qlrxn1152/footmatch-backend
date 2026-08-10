package com.dhoon.footmatch.member.validation;

import com.dhoon.footmatch.member.domain.Member;
import com.dhoon.footmatch.member.exception.exceptions.DuplicateUsernameException;
import com.dhoon.footmatch.member.exception.exceptions.InvalidUsernameException;
import com.dhoon.footmatch.member.exception.exceptions.NotFoundMemberException;
import com.dhoon.footmatch.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberValidator {

    private final MemberRepository memberRepository;

    public void validateDuplicateUsername(String username) {
        if ( memberRepository.existsByUsername(username)) {
            throw new DuplicateUsernameException();
        }
    }

    public Member validateExistMemberAndReturn(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);
    }

    public void validateUsernameContainStrip(String username) {
        if (!username.strip().equals(username)) {
            throw new InvalidUsernameException();
        }
    }


}
