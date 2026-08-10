# FootMatch


---

# Member

## 회원가입

### API

| Method | URL            | 설명   |
| ------ | -------------- | ---- |
| POST   | `/api/members` | 회원가입 |

### 호출 흐름

`MemberController.signup(MemberCreateRequest)`
→ `MemberService.signup(MemberCreateRequest)`

### 처리 과정

1. 회원이 아이디와 비밀번호를 입력합니다.
2. 아이디 형식을 검증합니다.

    * 길이: `4 ~ 12자`
    * 공백 포함 여부 확인
3. 비밀번호 형식을 검증합니다.

    * 길이: `4 ~ 15자`
4. 동일한 아이디가 이미 존재하는지 확인합니다.
5. 모든 검증을 통과하면 비밀번호를 해시화하여 DB에 저장합니다.

### 발생 가능한 예외

* `DuplicateUsernameException`

    * 이미 존재하는 아이디로 회원가입을 시도한 경우

* `InvalidUsernameException`

    * 아이디에 공백이 포함되어 있는 경우

---

## 마이페이지

### API

| Method | URL               | 설명            |
| ------ | ----------------- | ------------- |
| GET    | `/api/members/me` | 로그인한 회원 정보 조회 |

### 호출 흐름

`MemberController.me(JWT)`
→ `MemberService.findMember(memberId)`

### 처리 과정

1. 로그인한 회원이 마이페이지에 접근합니다.
2. 요청에 포함된 JWT의 유효성을 검증합니다.
3. JWT에서 회원 식별 정보를 추출합니다.
4. 해당 회원이 DB에 존재하는지 확인합니다.
5. 검증이 완료되면 회원 상세 정보인 `MemberDetail`을 반환합니다.

### 발생 가능한 예외

* `NotFoundMemberException`

    * JWT에 해당하는 회원을 찾을 수 없는 경우

---

# Auth

## 로그인

### API

| Method | URL               | 설명  |
| ------ | ----------------- | --- |
| POST   | `/api/auth/login` | 로그인 |

### 호출 흐름

`AuthController.login(MemberLoginRequest)`
→ `AuthService.login(MemberLoginRequest)`

### 처리 과정

1. 회원이 아이디와 비밀번호를 입력합니다.
2. 입력한 아이디에 해당하는 회원이 존재하는지 확인합니다.
3. 입력한 비밀번호와 저장된 비밀번호가 일치하는지 검증합니다.
4. 인증에 성공하면 JWT를 발급합니다.
5. 발급된 JWT의 유효시간은 `1시간`입니다.

### 발생 가능한 예외

* `InvalidLoginUsernameException`

    * 입력한 아이디와 일치하는 회원이 존재하지 않는 경우

* `InvalidLoginPasswordException`

    * 입력한 비밀번호가 저장된 비밀번호와 일치하지 않는 경우

---

# Team

## 팀 생성

### API

| Method | URL          | 설명   |
| ------ |--------------|------|
| POST   | `/api/teams` | 팀 생성 |

### 호출 흐름

`TeamController.createTeam(TeamCreateRequest, JWT)`
→ `JWT에서 memberId 추출`
→ `TeamService.createTeam(TeamCreateRequest, memberId)`


### 처리 과정

1. 로그인한 회원이 팀 이름을 입력합니다.
2. 회원이 존재하는지 확인합니다.
3. 요청한 회원이 이미 다른 팀에 속해있는지 확인합니다.
4. 팀 이름 형식을 검증합니다.
    * 길이: `2 ~ 20자`
5. 입력한 팀 이름이 이미 존재하는지 확인합니다.
6. 모든 검증을 통과하면 Team을 생성하여 저장합니다.
7. 생성한 팀과 요청한 회원을 이용해 TeamMember를 생성하여 저장합니다.
8. 해당 회원은 팀의 LEADER로 등록됩니다.

### 발생 가능한 예외

* `AlreadyJoinedTeamException`

    * 회원이 이미 팀에 속해있는 경우

* `InvalidTeamNameException`

    * 입력한 팀 이름이 길이 조건을 만족하지 않는 경우

* `DuplicateTeamNameException`

    * 입력한 팀 이름이 이미 존재하는경우

---