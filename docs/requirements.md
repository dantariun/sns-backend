# ShowMe Backend — 개발 요구사항 정의서

> 작성일: 2026-11-05  
> 목적: 일반 SNS 플랫폼 백엔드 포트폴리오 프로젝트 (공개용)  
> 연계 프론트: [sns-admin](../sns-admin) — 운영 관리자 대시보드

---

## 1. 프로젝트 개요

| 항목 | 내용 |
|------|------|
| 프로젝트명 | ShowMe Backend |
| 유형 | 포트폴리오 (공개용) |
| 언어 | Java 21 |
| 프레임워크 | Spring Boot 3.4.1 |
| 빌드 | Gradle 8.11.1 (Multi-module) |
| 구조 | Monorepo — core / api-admin / api-user |

---

## 2. 모듈 구성

| 모듈 | 포트 | 역할 |
|------|------|------|
| `core` | — | 공유 엔티티, 레포지토리, 공통 응답 형식, QueryDSL 설정 |
| `api-admin` | 8081 | 운영자 관리 API (sns-admin 프론트 연동) |
| `api-user` | 8080 | 사용자 앱 API |

---

## 3. 기능 범위

### 3.1 api-admin — 관리자 API

sns-admin 프론트엔드의 각 메뉴에 대응하는 API.

| 메뉴 | 세부 기능 |
|------|-----------|
| **인증** | 로그인, 액세스 토큰 재발급, 로그아웃 |
| **대시보드** | 가입자 추이, 게시물 추이, 미처리 신고 현황, 통계 요약 카드 |
| **회원 관리** | 목록(검색·필터·페이지네이션), 상세(프로필·피드·팔로워·팔로잉·제재 이력), 계정 정지/해제, 강제 탈퇴/복구 |
| **피드(게시물) 관리** | 목록(검색·필터), 상세(미디어·댓글·좋아요·신고), 블라인드 처리, 삭제 |
| **신고 관리** | 목록(처리 상태·유형 필터), 상세(신고 내용·대상), 처리(경고·블라인드·정지), 처리 이력 |
| **공지사항** | 목록, 상세, 작성, 수정, 삭제 |
| **FAQ** | 목록(카테고리 필터), 상세, 작성, 수정, 삭제 |

### 3.2 api-user — 사용자 API

| 카테고리 | 세부 기능 |
|----------|-----------|
| **인증** | SMS 인증, 로그인, 토큰 갱신, 로그아웃, FCM 토큰 등록, 닉네임 중복 확인 |
| **회원** | 프로필 조회/수정, 비밀번호 변경, 회원 탈퇴/취소, 좋아요·북마크·댓글 목록 |
| **팔로우** | 팔로우·언팔로우, 팔로워·팔로잉 목록 |
| **게시물** | 작성·조회·수정·삭제, 좋아요, 북마크, 신고, 관심없음, 팔로잉·추천 피드 |
| **댓글** | 작성·수정·삭제, 답글, 좋아요, 신고 |
| **검색** | 통합 검색, 자동완성, 인기 검색어, 검색 이력 |
| **알림** | 목록, 읽음 처리, 미읽음 수, 수신 설정, FCM 푸시 발송 |
| **채팅** | DM 채팅방 생성·목록, 메시지 송수신, 읽음 처리, WebSocket (STOMP) |
| **파일 업로드** | 이미지·오디오·비디오 → AWS S3 |
| **공지사항** | 목록, 상세, 미읽음 수, 읽음 처리 |
| **FAQ** | 목록 |
| **1:1 문의** | 작성, 내 문의 목록, 상세 |

---

## 4. 공통 요구사항

### 4.1 인증 & 보안

- JWT Stateless 인증 (Access Token 1시간, Refresh Token 8시간)
- Spring Security 6 기반 필터 체인
- 관리자 / 사용자 Role 분리
- BCrypt 비밀번호 암호화

### 4.2 API 응답 형식

```json
// 성공 — 단건
{ "code": "SUCCESS", "data": { } }

// 성공 — 페이지
{
  "code": "SUCCESS",
  "data": {
    "content": [],
    "totalElements": 120,
    "totalPages": 6,
    "number": 0,
    "size": 20
  }
}

// 실패
{ "code": "ERROR_CODE", "message": "에러 메시지" }
```

### 4.3 DB & ORM

- MySQL 8.4
- Spring Data JPA + QueryDSL 5.1.0
- Flyway 마이그레이션 (스테이징/운영 환경)
- DDL auto: `validate`

### 4.4 외부 연동

| 서비스 | 용도 |
|--------|------|
| Firebase Admin SDK (FCM) | 팔로우·좋아요·댓글 등 SNS 활동 푸시 알림 |
| AWS S3 | 프로필 이미지·게시물 미디어 파일 업로드 |

### 4.5 환경 분리

| 환경 | 프로파일 | DB |
|------|---------|-----|
| 로컬 | `local` | localhost:3307 |
| Docker | `docker` | 컨테이너 내부 |
| 운영 | `prod` | RDS (AWS) |

---

## 5. 기술 스택

| 분류 | 기술 | 버전 |
|------|------|------|
| Language | Java | 21 |
| Framework | Spring Boot | 3.4.1 |
| Build | Gradle (Multi-module) | 8.11.1 |
| ORM | Spring Data JPA + QueryDSL | — / 5.1.0 |
| DB | MySQL | 8.4 |
| Auth | JWT (jjwt) | 0.12.6 |
| Security | Spring Security | 6 |
| Docs | SpringDoc OpenAPI (Swagger) | 2.8.6 |
| Push | Firebase Admin SDK (FCM) | 9.3.0 |
| Storage | AWS S3 (Spring Cloud AWS) | 3.2.1 |
| Realtime | Spring WebSocket + STOMP | — |
| Container | Docker + Docker Compose | — |
| CI/CD | GitHub Actions | — |

---

## 6. 브랜치 전략 & 커밋 컨벤션

```
main        기준 브랜치 — PR 병합만
feature/*   기능 개발
fix/*       버그 수정
docs/*      문서 작업
```

| 타입 | 설명 |
|------|------|
| `chore` | 초기화, 빌드 설정, 의존성 |
| `feat` | 기능 구현 |
| `fix` | 버그 수정 |
| `docs` | 문서 |
| `test` | 테스트 |
| `style` | 코드 스타일 (기능 변경 없음) |
| `refactor` | 리팩토링 |

---

## 7. 로컬 실행 방법

### 사전 요구사항

- JDK 21
- Docker & Docker Compose (MySQL 컨테이너용)

### 1. DB 컨테이너 기동

```bash
docker-compose -f docker-compose.dev.yml up -d
```

### 2. 환경 설정 복사

```bash
cp api-admin/src/main/resources/application-local.yml.example \
   api-admin/src/main/resources/application-local.yml
# 비밀번호·키 직접 입력
```

### 3. api-admin 실행

```bash
./gradlew :api-admin:bootRun --args='--spring.profiles.active=local'
# Swagger UI: http://localhost:8081/swagger-ui/index.html
```

### 4. api-user 실행

```bash
./gradlew :api-user:bootRun --args='--spring.profiles.active=local'
# Swagger UI: http://localhost:8080/swagger-ui/index.html
```
