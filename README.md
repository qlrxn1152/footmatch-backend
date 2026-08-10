# FootMatch

## 기술 스택

- Java
- Spring Boot
- Spring Security 
- JPA
- MySQL
- Flutter

---

# MemberController 

# 회원가입 

| Method | URL          | 설명   |
|---|--------------|------|
| POST | /api/members | 회원가입 |

>MemberController signup(MemberCreateRequest) -> MemberService signup(MemberCreateRequest)



- 회원이 아이디와 비밀번호를 입력
- 아이디 4~12글자 인지 검증 / 비밀번호가 4~15글자인지 검증 / 아이디가 이미 존재하는지 검증
- 해당 검증들 통과시, 비밀번호를 해쉬화해서 DB 에 저장.


# 발생가능한 예외

> DuplicateUsernameException [아이디 중복시]

> InvalidUsernameException [아이디에 공백 포함시]

---

# 마이페이지

| Method | URL          | 설명      |
|--------|--------------|---------|
| GET    | /api/members/me| 마이페이지   |

>MemberController login(Jwt) -> MemberService login(memberId)


- 로그인한 회원이 상세페이지를 클릭 
- Jwt 가 위조가되지않았는지 검증 / 해당멤버가 존재하는지 확인
- 해당 검증들 통과시, MemberDetail 전달 => 상세페이지

# 발생가능한 예외

> NotFoundMemberException [멤버 조회 실패시]


---

# 로그인

| Method | URL             | 설명   |
|---|-----------------|------|
| POST | /api/auth/login | 회원가입 |

>AuthController login(MemberLoginRequest) -> AuthService login(MemberLoginRequest)


- 회원이 아이디와 비밀번호를 입력
- 입력한 아이디가 존재하는지 검증 / 입력한 비밀번호가 맞는지 검증
- 해당 검증들 통과시, JWT 토큰을 발급 ( 유효시간 = 1시간 )


# 발생가능한 예외

> InvalidLoginUsernameException [아이디 불일치시]

> InvalidLoginPasswordException [비밀번호 불일치시]

---



