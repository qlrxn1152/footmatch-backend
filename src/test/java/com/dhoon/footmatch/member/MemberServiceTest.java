package com.dhoon.footmatch.member;

import com.dhoon.footmatch.member.domain.Member;
import com.dhoon.footmatch.member.dto.response.MemberCreateResponse;
import com.dhoon.footmatch.member.exception.exceptions.DuplicateUsernameException;
import com.dhoon.footmatch.member.exception.exceptions.InvalidUsernameException;
import com.dhoon.footmatch.member.repository.MemberRepository;
import com.dhoon.footmatch.member.service.MemberService;
import com.dhoon.footmatch.support.IntegrateTest;
import com.dhoon.footmatch.support.fixture.MemberFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@IntegrateTest
class MemberServiceTest {

    @Autowired private MemberService memberService;
    @Autowired private MemberFixture memberFixture;

    @Autowired private MemberRepository memberRepository;

    @Autowired private PasswordEncoder encoder;



    @Nested
    class MemberSignup {

        @Test
        @DisplayName(value = "회원가입 성공")
        void signup() throws Exception {
            // given
            String username = "userA";
            String password = "1234";

            // when
            MemberCreateResponse response = memberFixture.signupMember(username, password);
            Member memberEntity = memberRepository.findById(response.getMemberId()).get();

            // then
            assertThat(response.getUsername()).isEqualTo(username);
            assertThat(memberEntity.getUsername()).isEqualTo(username);
            assertThat(encoder.matches(password, memberEntity.getPassword())).isTrue();
        }




        @Test
        @DisplayName(value = "회원가입 실패_공백")
        void signup_fail_strip() throws Exception {
            // given
            String username = "     userA  ";
            String password = "1234";

            // when
            assertThatThrownBy(() -> memberFixture.signupMember(username, password))
                    .isInstanceOf(InvalidUsernameException.class)
                    .hasMessage("공백은 허용하지 않습니다.");
        }



        @Test
        @DisplayName(value = "회원가입_실패_중복 이름")
        void signup_fail_duplicateUsername() throws Exception {
            // given
            String username = "userA";
            String password = "1234";

            memberFixture.signupMember(username, password);

            // when && then
            assertThatThrownBy(() -> memberFixture.signupMember("userA", "q1w2e3"))
                    .isInstanceOf(DuplicateUsernameException.class)
                    .hasMessage("아이디 중복");
        }
    }

}