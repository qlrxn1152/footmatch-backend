# Footmatch Railway 배포

## 현재 준비 범위

Java 25 Docker 빌드, prod 프로필, MySQL/JWT 환경변수, Flutter 웹 CORS,
DB 연결까지 확인하는 `/actuator/health`를 준비했습니다.
이 변경만으로 Railway 서비스나 Flutter 배포가 변경되지는 않습니다.

## 1. 빈 데이터베이스 준비

기존 Railway MySQL 서비스 안에 Footmatch 전용 데이터베이스를 만듭니다.
MySQL 서버를 추가로 만들 필요는 없습니다. 기존 FootballV2 테이블에 연결하지 않습니다.
관리 권한이 있는 MySQL 연결에서 먼저 확인합니다.

```sql
SHOW DATABASES LIKE 'footmatch';
```

없을 때만 다음을 실행합니다. 이미 있다면 안의 테이블을 확인하고,
비어 있지 않으면 다른 새 이름을 정해 아래 JDBC URL에도 반영합니다.

```sql
CREATE DATABASE footmatch CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

기존 볼륨에서 `MYSQL_DATABASE` 값만 변경하는 것은 새 데이터베이스 생성 작업을 대신하지 않습니다.
기존 서비스·볼륨·테이블을 삭제하는 명령은 이 절차에 없습니다.
MySQL 접속 정보는 Railway의 해당 서비스에서 확인하고 비밀번호는 채팅이나 Git에 올리지 않습니다.

## 2. 백엔드 서비스 환경변수

아래는 MySQL 서비스 이름이 `MySQL`인 경우입니다.
다르면 `${{MySQL.변수명}}`의 서비스 이름도 실제 이름에 맞춥니다.
새 DB 접근 권한이 있는 계정인지 확인합니다.

| 이름 | 값 |
|---|---|
| `SPRING_PROFILES_ACTIVE` | `prod` |
| `PORT` | `8080` (기존 공개 도메인 대상 포트와 일치) |
| `DB_URL` | `jdbc:mysql://${{MySQL.MYSQLHOST}}:${{MySQL.MYSQLPORT}}/footmatch?serverTimezone=Asia/Seoul&characterEncoding=UTF-8` |
| `DB_USERNAME` | `${{MySQL.MYSQLUSER}}` |
| `DB_PASSWORD` | `${{MySQL.MYSQLPASSWORD}}` |
| `ISSUER` | `footmatch` |
| `JWT_SECRET` | 로컬에서 생성한 Base64 32바이트 이상의 비밀키 |
| `CORS_ALLOWED_ORIGINS` | `https://qlrxn1152.github.io` |

Mac 터미널에서 비밀키 생성:

```bash
openssl rand -base64 32
```

출력값은 Railway의 `JWT_SECRET`에만 입력합니다.
CORS 값에는 `/footballv2-flutter/` 같은 경로나 마지막 `/`를 넣지 않습니다.
여러 웹 주소는 쉼표로 구분합니다. 로컬 웹 개발은 고정 포트를 정한 뒤
`application-local.properties`의 `app.cors.allowed-origins`에 해당 origin을 설정합니다.

기존 서비스에는 `SPRING_DATASOURCE_*`, `SPRING_JPA_*`, `JWT_*` 또는
시작 명령처럼 새 설정을 덮어쓰는 값이 남아 있을 수 있습니다.
배포 전 기존 설정을 확인하여 같은 목적의 상충하는 값을 정리합니다.

## 3. 저장소 전환

먼저 이 변경의 빌드/테스트를 확인합니다. 배포할 기능 범위도 확인한 뒤
Railway Source를 `qlrxn1152/footmatch-backend`의 준비된 브랜치로 연결합니다.
연결 변경으로 배포가 시작될 수 있으므로 DB와 환경변수를 먼저 준비합니다.

`Dockerfile`과 `railway.toml`이 저장소 루트에 있습니다.
Root Directory는 루트로 두고, 예전 프로젝트 전용 Build/Start/Pre-deploy 명령은 제거합니다.
Dockerfile의 ENTRYPOINT가 서버를 실행합니다.
공개 도메인의 대상 포트는 `8080`으로 맞춥니다.
기존 주소의 footballv2 문자열은 실행할 코드와 별개이므로 주소 변경은 연결 확인 후 진행할 수 있습니다.

## 4. 정상 결과와 문제 확인

배포 후 `https://<공개 도메인>/actuator/health`에서 다음 응답을 확인합니다.

```json
{"status":"UP"}
```

| 현상 | 확인할 항목 |
|---|---|
| 빌드 실패 | Build logs의 첫 원인, Java 25/Dockerfile 사용 여부 |
| `Unknown database` | 빈 DB 생성 여부와 `DB_URL`의 DB 이름 |
| `Access denied` | 계정, 비밀번호, 새 DB 접근 권한 |
| `Could not resolve placeholder` | prod 프로필과 필수 환경변수 |
| JWT 키 오류 | `JWT_SECRET`이 Base64 형식이며 디코딩 후 32바이트 이상인지 |
| Healthcheck 실패/502 | 실행 로그, PORT와 도메인 대상 포트, DB 연결 |
| 웹 CORS 오류 | 정확한 프런트 origin, 허용 헤더, Flutter API 주소 |
| 로그인 후 401 | 새 서버에서 다시 로그인, 토큰 전달과 issuer |

`ddl-auto=update`는 빈 DB에 테이블을 생성하고 재시작 시 데이터를 유지하기 위한 초기 데모 설정입니다.
실제 데이터를 운영하며 스키마를 변경하기 전에는 버전별 마이그레이션을 도입합니다.
`create` 또는 `create-drop`으로 변경하지 않습니다.

## 5. Flutter는 별도 연결 작업

기존 Flutter 화면은 구버전 API를 사용합니다. 백엔드 주소 변경 외에
회원가입 URL, 응답 DTO, JWT 헤더, 팀/경기 API를 맞춘 뒤 웹을 다시 빌드해야 합니다.
백엔드 헬스 확인만으로 Flutter 연결까지 완료된 것은 아닙니다.

## 검증 명령

Java 25 환경에서 실행합니다.

```bash
./gradlew --no-daemon test --tests com.dhoon.footmatch.common.DeploymentHttpTest bootJar
```

추가 테스트는 공개 헬스 응답, 기존 API 인증 유지, 허용 origin의 preflight,
미허용 origin 거절을 확인합니다. 배포 시 실제 MySQL 연결 검증도 필요합니다.

참고: [Railway Healthchecks](https://docs.railway.com/deployments/healthchecks),
[Railway MySQL](https://docs.railway.com/databases/mysql),
[Spring Security CORS](https://docs.spring.io/spring-security/reference/servlet/integrations/cors.html).
