# ShowMe Backend — 개발 로드맵

> 작성일: 2026-11-05  
> 연계 프론트: [sns-admin](../sns-admin)

---

## Phase 1 — 프로젝트 셋업 & core 모듈 ✅

**목표**: 멀티모듈 뼈대 + 공유 엔티티 + DB 연결 완성  
**완료일**: 2026-11-11

### 1-1. 프로젝트 초기화
- [x] Spring Boot 3.4.1 + Java 21 멀티모듈 Gradle 프로젝트 생성
- [x] 모듈 구성: core / api-admin / api-user
- [x] Gradle wrapper 설정
- [x] .gitignore 설정 (민감 정보 제외)
- [x] docker-compose.dev.yml (로컬 MySQL 컨테이너)

### 1-2. core 모듈 — 공유 엔티티
- [x] 공통 응답 형식 (`ApiResponse<T>`, `PageResponse<T>`)
- [x] 공통 예외 (`BusinessException`, `ErrorCode`)
- [x] 글로벌 예외 핸들러 (`GlobalExceptionHandler`)
- [x] JPA BaseEntity (createdAt, updatedAt)
- [x] Member 엔티티 (회원 기본 정보)
- [x] QueryDSL 설정

### 1-3. 공통 설정
- [x] application.yml / application-local.yml.example
- [x] DataSource, JPA 설정
- [x] README.md 초안

---

## Phase 2 — api-admin 인증 & Spring Security ✅

**목표**: 관리자 JWT 인증 흐름 완성  
**완료일**: 2026-11-16

- [x] JWT Provider (토큰 생성·검증·파싱, access/refresh 타입 구분)
- [x] JwtAuthenticationFilter
- [x] Spring Security Config (관리자 Role — SUPER / ADMIN, CORS, 401·403 JSON 응답)
- [x] AdminMember 엔티티 + Repository
- [x] AdminRefreshToken 엔티티 (Refresh Token Rotation)
- [x] Flyway `V1` 마이그레이션 (member · admin_member · admin_refresh_token)
- [x] `POST /admin/auth/login` — 로그인
- [x] `POST /admin/auth/refresh` — 토큰 재발급
- [x] `POST /admin/auth/logout`
- [x] Swagger UI 연동 (Bearer 인증 스킴)

**검증**: 로컬 MySQL 컨테이너에서 기동 후 로그인 → 재발급 → 사용된 refresh 재사용 거부 → 로그아웃 흐름 확인

**작업 중 발견·수정**
- core의 엔티티·리포지토리가 앱 모듈에서 스캔되지 않던 문제 → `JpaConfig`에 스캔 범위 명시
- 같은 초에 재발급하면 동일한 refresh 토큰이 생성돼 유니크 제약 위반 → 토큰에 `jti`(UUID) 추가

---

## Phase 3 — api-admin 회원 관리 ✅

**목표**: sns-admin 회원 메뉴 API 완성  
**완료일**: 2026-11-17

- [x] MemberSanction 엔티티 (제재 이력 — 정지·해제·강제 탈퇴·복구)
- [x] Follow 엔티티, 기본 Post 엔티티 (회원 상세의 팔로워·피드 수 집계용으로 선행 도입)
- [x] Flyway `V2` 마이그레이션 (member.suspended_until · member_sanction · follow · post)
- [x] `GET /admin/members` — 목록 (닉네임·이메일 검색, 상태·가입일 필터, 페이지네이션, QueryDSL)
- [x] `GET /admin/members/{id}` — 상세 (프로필 + 팔로워·팔로잉·피드 수)
- [x] `GET /admin/members/{id}/posts` — 회원 피드 (삭제·블라인드 포함)
- [x] `GET /admin/members/{id}/followers` — 팔로워
- [x] `GET /admin/members/{id}/followings` — 팔로잉
- [x] `GET /admin/members/{id}/sanction-history` — 제재 이력
- [x] `PATCH /admin/members/{id}/sanction` — 계정 정지 (기간 지정 / 무기한)
- [x] `PATCH /admin/members/{id}/sanction/release` — 정지 해제
- [x] `PATCH /admin/members/{id}/withdraw` — 강제 탈퇴
- [x] `PATCH /admin/members/{id}/restore` — 계정 복구

**검증**: 로컬 MySQL에 테스트 회원·팔로우·게시물을 넣고 검색·필터·페이지·집계·상태 전이(정지 → 해제, 탈퇴 → 복구)와 잘못된 전이 거부, 제재 이력 기록을 확인

---

## Phase 4 — api-admin 피드·신고·공지·FAQ ✅

**목표**: sns-admin 콘텐츠·신고·고객센터 메뉴 API 완성  
**완료일**: 2026-11-18

### 4-1. 피드(게시물) 관리
- [x] Post 확장 (공개 범위 · 미디어 · 태그) / PostComment / PostLike 엔티티
- [x] `GET /admin/posts` — 목록 (본문·작성자 검색, 상태 PUBLIC·PRIVATE·BLINDED, 작성일 필터, 신고 수)
- [x] `GET /admin/posts/{id}` — 상세 (미디어·태그·신고 수)
- [x] `GET /admin/posts/{id}/comments` — 댓글 목록 (답글·삭제·블라인드 포함)
- [x] `GET /admin/posts/{id}/likes` — 좋아요 목록
- [x] `PATCH /admin/posts/{id}/blind` · `/unblind` — 블라인드 처리·해제
- [x] `DELETE /admin/posts/{id}` — 삭제 (소프트 삭제)
- [x] `DELETE /admin/comments/{id}` — 댓글 삭제 (게시물 댓글 수 차감)

### 4-2. 신고 관리
- [x] Report 엔티티 (대상: FEED · COMMENT · MEMBER)
- [x] `GET /admin/reports` — 목록 (처리 상태·대상 유형 필터)
- [x] `GET /admin/reports/{id}` — 상세 (대상 요약 + 같은 대상 누적 신고 수)
- [x] `GET /admin/reports/{id}/history` — 처리 이력 (같은 대상의 신고와 처리 결과)
- [x] `PATCH /admin/reports/{id}` — 처리 (경고 · 블라인드 · 정지 · 기각, 정지 시 제재 이력 기록)

### 4-3. 공지사항
- [x] Notice 엔티티
- [x] `GET /admin/notices` — 목록 (제목 검색, 게시 여부 필터)
- [x] `GET /admin/notices/{id}` — 상세
- [x] `POST /admin/notices` — 작성
- [x] `PUT /admin/notices/{id}` — 수정
- [x] `DELETE /admin/notices/{id}` — 삭제 (소프트 삭제)

### 4-4. FAQ
- [x] Faq 엔티티
- [x] `GET /admin/faqs` — 목록 (질문·답변 검색, 카테고리 필터)
- [x] `GET /admin/faqs/{id}` — 상세
- [x] `POST /admin/faqs` — 작성
- [x] `PUT /admin/faqs/{id}` — 수정
- [x] `DELETE /admin/faqs/{id}` — 삭제 (소프트 삭제)

- [x] Flyway `V3` 마이그레이션 (post 확장 · post_media · post_tag · post_comment · post_like · report · notice · faq)

**검증**: 로컬 MySQL에 게시물·댓글·좋아요·신고 데이터를 넣고 검색·필터·블라인드·삭제, 신고 처리 4종의 실제 반영(게시물·댓글 블라인드, 작성자 정지와 제재 이력), 잘못된 처리 거부, 공지·FAQ CRUD를 확인

**작업 중 발견·수정**
- 잘못된 enum 값·깨진 JSON이 500으로 응답되던 문제 → 400으로 처리
- 잘못된 검색 조건이 내부 클래스명이 담긴 메시지로 응답되던 문제 → 필드명만 알려주도록 정리

---

## Phase 5 — api-admin 대시보드 통계

**목표**: sns-admin 대시보드 메뉴 API 완성

- [ ] `GET /admin/stats/summary` — 통계 요약 카드 (총 회원, 오늘 가입자, 총 게시물, 미처리 신고)
- [ ] `GET /admin/stats/members/trend` — 가입자 추이 (기간별)
- [ ] `GET /admin/stats/posts/trend` — 게시물 추이 (기간별)
- [ ] `GET /admin/stats/reports/pending` — 미처리 신고 현황

---

## Phase 6 — api-user 인증 & 회원 & 팔로우

**목표**: 사용자 JWT 인증 + 소셜 프로필 기능 완성

### 6-1. 사용자 인증
- [ ] 사용자 JWT Provider / Filter (관리자와 분리)
- [ ] Spring Security Config (사용자 Role)
- [ ] `POST /auth/sms/send` — SMS 인증 발송
- [ ] `POST /auth/login` — 로그인
- [ ] `POST /auth/refresh` — 토큰 재발급
- [ ] `POST /auth/logout`
- [ ] `POST /auth/fcm-token` — FCM 토큰 등록
- [ ] `GET /auth/check/nickname` — 닉네임 중복 확인

### 6-2. 회원 프로필
- [ ] `GET /users/me` — 내 프로필
- [ ] `PUT /users/me` — 프로필 수정
- [ ] `PUT /users/me/password` — 비밀번호 변경
- [ ] `DELETE /users/me` — 탈퇴
- [ ] `GET /users/{memberId}` — 타인 프로필
- [ ] `GET /users/me/liked-posts` — 좋아요한 게시물
- [ ] `GET /users/me/bookmarks` — 북마크 목록
- [ ] `POST /users/{memberId}/block` / `DELETE` — 차단/해제
- [ ] `POST /users/{memberId}/report` — 신고

### 6-3. 팔로우
- [x] Follow 엔티티 (Phase 3에서 선행 도입)
- [ ] `POST /users/{id}/follow` — 팔로우
- [ ] `DELETE /users/{id}/follow` — 언팔로우
- [ ] `GET /users/{id}/followers` — 팔로워 목록
- [ ] `GET /users/{id}/following` — 팔로잉 목록

---

## Phase 7 — api-user 게시물 & 댓글 & 검색

**목표**: SNS 핵심 콘텐츠 API + 파일 업로드 완성

### 7-1. 파일 업로드 (AWS S3)
- [ ] `POST /files/image` — 이미지 업로드
- [ ] `POST /files/video` — 비디오 업로드
- [ ] `POST /files/audio` — 오디오 업로드

### 7-2. 게시물
- [ ] Post / PostLike / PostBookmark 엔티티
- [ ] `POST /posts` — 작성
- [ ] `GET /posts/{id}` — 상세
- [ ] `PUT /posts/{id}` — 수정
- [ ] `DELETE /posts/{id}` — 삭제
- [ ] `GET /posts/feed/following` — 팔로잉 피드
- [ ] `GET /posts/feed/recommend` — 추천 피드
- [ ] `POST /posts/{id}/like` / `DELETE` — 좋아요
- [ ] `POST /posts/{id}/bookmark` / `DELETE` — 북마크
- [ ] `POST /posts/{id}/report` — 신고
- [ ] `POST /posts/{id}/not-interested` — 관심없음

### 7-3. 댓글
- [ ] PostComment / CommentLike 엔티티
- [ ] `POST /posts/{id}/comments` — 댓글 작성
- [ ] `GET /posts/{id}/comments` — 댓글 목록
- [ ] `GET /posts/comments/{id}/replies` — 답글 목록
- [ ] `PUT /posts/comments/{id}` — 수정
- [ ] `DELETE /posts/comments/{id}` — 삭제
- [ ] `POST /posts/comments/{id}/like` / `DELETE` — 좋아요
- [ ] `POST /posts/comments/{id}/report` — 신고

### 7-4. 검색
- [ ] `GET /search` — 통합 검색
- [ ] `GET /search/suggestions` — 자동완성
- [ ] `GET /search/popular` — 인기 검색어
- [ ] `GET /search/history` — 검색 이력
- [ ] `DELETE /search/history/{id}` — 이력 삭제

---

## Phase 8 — api-user 알림(FCM) & 채팅

**목표**: 실시간 알림 파이프라인 + WebSocket 채팅 완성

### 8-1. 알림 (FCM 연동)
- [ ] NewsItem 엔티티 (알림 목록)
- [ ] FcmService — Firebase Admin SDK 연동, 푸시 발송
- [ ] NotificationWriter — 알림 INSERT + FCM 발송 묶음
- [ ] 팔로우 알림 (FollowService 트리거)
- [ ] 게시물·댓글 좋아요 알림 (자기 글 제외)
- [ ] 댓글·답글 알림
- [ ] `GET /notification` — 알림 목록
- [ ] `GET /notification/unread-count` — 미읽음 수
- [ ] `PATCH /notification/{id}/read` — 읽음 처리
- [ ] `PUT /notification/settings` — 알림 수신 설정

### 8-2. 채팅 (WebSocket)
- [ ] ChatRoom / ChatMessage 엔티티
- [ ] WebSocket + STOMP 설정
- [ ] `POST /chat/rooms` — DM 채팅방 생성
- [ ] `GET /chat/rooms` — 채팅방 목록
- [ ] `GET /chat/rooms/{id}/messages` — 메시지 목록
- [ ] `PATCH /chat/rooms/{id}/read` — 읽음 처리
- [ ] `GET /chat/rooms/unread-count` — 미읽음 수
- [ ] WebSocket 실시간 메시지 송수신 (`/pub/chat/{roomId}`)

### 8-3. 고객센터 (api-user)
- [ ] `GET /notices` — 공지사항 목록
- [ ] `GET /notices/{id}` — 공지사항 상세
- [ ] `GET /faqs` — FAQ 목록
- [ ] `POST /inquiries` — 1:1 문의 작성
- [ ] `GET /inquiries/me` — 내 문의 목록

---

## Phase 9 — 테스트 & CI/CD & 마무리

**목표**: 품질 검증 + 자동 배포 + 포트폴리오 완성

### 9-1. 테스트
- [ ] JUnit 5 + Mockito 단위 테스트 (핵심 서비스 60% 이상)
- [ ] `@DataJpaTest` 레포지토리 테스트 (H2)
- [ ] `@WebMvcTest` API 계층 테스트

### 9-2. CI/CD
- [ ] GitHub Actions PR 빌드 검증 (`./gradlew build`)
- [ ] main 머지 시 EC2 자동 배포
- [ ] GitHub Secrets로 환경변수 관리

### 9-3. 마무리
- [ ] README.md 완성 (기술 스택, Swagger 링크, 로컬 실행)
- [ ] Swagger 주석 완성 (각 API 설명)
- [ ] 아키텍처 다이어그램 추가

---

## 기술 스택 요약

| 분류 | 기술 | 버전 |
|------|------|------|
| Language | Java | 21 |
| Framework | Spring Boot | 3.4.1 |
| Build | Gradle (Multi-module) | 8.11.1 |
| ORM | Spring Data JPA + QueryDSL | — / 5.1.0 |
| DB | MySQL | 8.4 |
| Auth | JWT (jjwt) | 0.12.6 |
| Security | Spring Security | 6 |
| Docs | SpringDoc OpenAPI | 2.8.6 |
| Push | Firebase Admin SDK (FCM) | 9.3.0 |
| Storage | AWS S3 (Spring Cloud AWS) | 3.2.1 |
| Realtime | Spring WebSocket + STOMP | — |
| Container | Docker + Docker Compose | — |
| CI/CD | GitHub Actions → EC2 | — |
