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
4. 정규화된 팀 이름 형식을 검증합니다. ( 정규화 => 앞뒤 공백제거)
    * 길이: `2 ~ 20자`
5. 정규화된 팀 이름이 이미 존재하는지 확인합니다.
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

## 팀 이름 변경

### API

| Method | URL                        | 설명     |
|--------|----------------------------|--------|
| PATCH  | `/api/teams/{teamId}/name` | 팀 이름변경 |

### 호출 흐름

`TeamController.changeTeamName(TeamNameChangeRequest, JWT, teamId)`
→ `JWT에서 memberId 추출`
→ `TeamService.changeTeamName(TeamNameChangeRequest, memberId, teamId)`


### 처리 과정 

1. 로그인한 회원이 변경할 팀 이름을 입력합니다.
2. 회원이 존재하는지 확인합니다.
3. 팀이 존재하는지 확인합니다.
4. 회원이 어떤 팀에 가입되어 있는지 확인합니다.
5. 회원이 요청한 teamId의 팀에 속한 회원인지 확인합니다.
6. 회원이 해당 팀의 LEADER인지 확인합니다.
7. 입력한 팀 이름의 앞뒤 공백을 제거하여 정규화합니다.
8. 정규화된 팀 이름의 길이를 검증합니다.
    - 길이: `2 ~ 20자`
9. 정규화된 팀 이름이 현재 팀 이름과 동일한지 확인합니다.
10. 정규화된 팀 이름을 다른 팀이 이미 사용 중인지 확인합니다.
11. 모든 검증을 통과하면 팀 이름을 변경합니다.

### 발생 가능한 예외

- `NotFoundMemberException`
    - 요청한 회원이 존재하지 않는 경우

- `NotFoundTeamException`
    - 변경 대상 팀이 존재하지 않는 경우

- `NotJoinedTeamException`
    - 회원이 어떤 팀에도 가입되어 있지 않은 경우

- `NotTeamMemberException`
    - 회원이 다른 팀에는 속해있지만 변경 대상 팀의 팀원이 아닌 경우

- `NotTeamLeaderException`
    - 회원이 변경 대상 팀의 LEADER가 아닌 경우

- `SameTeamNameException`
    - 변경할 팀 이름이 현재 팀 이름과 동일한 경우

- `InvalidTeamNameException`
    - 정규화된 팀 이름이 길이 조건을 만족하지 않는 경우

- `DuplicateTeamNameException`
    - 다른 팀이 동일한 팀 이름을 이미 사용하고 있는 경우
---

## 팀 상세페이지

### API

| Method | URL                   | 설명      |
|--------|-----------------------|---------|
| GET    | `/api/teams/{teamId}` | 팀 상세페이지 |


### 명세

- 누구나 해당 기능을 호출할 수 있습니다.

### 검증

- 요청한 `teamId`에 해당하는 팀이 존재하는지 확인합니다.
- 팀이 존재하지 않으면 `NotFoundTeamException`을 발생시킵니다.

### 반환값

- 팀 ID
- 팀 이름
- 팀장 ID
- 팀장 이름
- 팀 레이팅
- 팀원 수


### 호출 흐름

`TeamController.getTeam(teamId)`
→ `TeamService.getTeam(teamId)`



### 처리 과정

1. 사용자가 팀 상세페이지를 조회합니다.
2. `@PathVariable`로 전달받은 `teamId`를 이용해 팀을 조회합니다.
3. 팀이 존재하지 않으면 `NotFoundTeamException`을 발생시킵니다.
4. 조회한 팀의 상세 정보를 DTO 형태로 반환합니다.


### 발생 가능한 예외

- `NotFoundTeamException`
    - 요청한 `teamId`에 해당하는 팀이 존재하지 않는 경우

---


## 팀장 변경

### API

| Method | URL                          | 설명    |
|--------|------------------------------|-------|
| PATCH  | `/api/teams/{teamId}/leader` | 팀장 변경 |


### 권한

- 현재 팀장만 팀장을 위임할 수 있습니다.


### 검증

- 요청한 회원이 존재하는가 ? 
- 팀이 존재하는가 ?
- 요청자가 해당 팀의 팀원인가 ? 
- 요청자가 해당 팀의 팀장인가 ?
- 새로운 팀장이 될 회원이 존재하는가 ? 
- 새로운 팀장이 해당 팀의 팀원인가 ? 
- 현재 팀장과 새로운 팀장이 동일한 회원이 아닌가 ?



### 호출 흐름

`TeamController.transferLeader(teamId, Jwt, TeamLeaderTransferRequest)`
→ `JWT에서 현재팀장 memberId 추출`
→ `TeamService.transferLeader(teamId, Jwt MemberId, TeamLeaderTransferRequest)`



### 처리 과정

1. 요청한 회원이 새로운 팀장이 될 회원을 입력
2. 요청한 회원이 존재하는 회원이 맞는지
3. 해당 팀이 존재하는지
4. 요청자는 해당 팀의 팀원이 맞는지
5. 요청자는 해당 팀의 팀장이 맞는지
6. 새로운 팀장이 될 회원은 존재하는지 
7. 새로운 팀장이 될 회원은 해당팀의 팀원인지
8. 새로운 팀장이 될 회원이 요청한 회원과 같은 회원이 아닌지
9. 팀장변경



### 발생 가능한 예외

- `NotFoundTeamException`
    - 요청한 `teamId`에 해당하는 팀이 존재하지 않는 경우

- `NotFoundMemberException`
    - 현재 팀장 또는 새로운 팀장이 될 회원이 존재하지 않는 경우

- `NotJoinedTeamException`
    - 요청한 회원과, 새로운 팀장이 팀에 속해있지 않은 경우

- `NotTeamLeaderException`
    - 요청한 회원이 팀장이 아닌경우

- `NotTeamMemberException`
    - 요청한 회원과, 새로운 팀장이 될 회원이 해당팀의 소속이 아닌경우

- `SameTeamLeaderException`
    - 현재 팀장과 새로운 팀장이 될 회원이 동일한 경우
---

# Domain Policy

## Team


### 팀장 정보 기준


- 하나의 팀에는 한 명의 팀장이 존재합니다.
- 팀장의 단일 기준은 `Team.leaderMember`입니다.
- `TeamRole`은 팀장 여부를 표현하지 않습니다.
- `TeamRole`은 `MEMBER`, `STAFF` 역할만 표현합니다.
- 팀장은 `TeamRole`과 별개로 `Team.leaderMember`를 통해 관리합니다.

### 팀 소속 정보 기준

- 회원의 팀 소속 여부는 `TeamMember`를 기준으로 판단합니다.
- 회원이 특정 팀에 속해있는지 여부도 `TeamMember`를 통해 확인합니다.