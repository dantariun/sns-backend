# ShowMe Backend

SNS 플랫폼 백엔드 포트폴리오 프로젝트.  
Spring Boot 멀티모듈 구조로 관리자 API(api-admin)와 사용자 API(api-user)를 단일 레포지토리에서 운영한다.

> [!IMPORTANT]
> **이 저장소의 코드는 한 줄도 직접 타이핑하지 않았습니다.**
> 설계 방향과 기술 판단을 자연어 프롬프트로 지시하고, [Claude Code](https://claude.com/claude-code)가
> 에이전트·스킬·자동화 도구를 활용해 코드·문서·커밋을 전부 작성했습니다.

연관 프로젝트: [loop-sns-showcase](https://github.com/dantariun/loop-sns-showcase) (React Native 앱) · [sns-admin-dashboard](https://github.com/dantariun/sns-admin-dashboard) (관리자 웹)

---

## 어떻게 만들어졌나 — AI 에이전트 기반 개발

sns-app과 같은 방향성으로, **"프롬프트와 에이전트만으로 실무 수준의 백엔드를 단계적으로 쌓아 올릴 수 있는가"** 를 산출물로 남기는 프로젝트입니다.

### 역할 분담

| 사람(프롬프트) | 에이전트(Claude Code) |
|---|---|
| 무엇을 만들지 — "운영 중인 실무 백엔드를 공개용 포트폴리오로" | 실무 프로젝트 분석, 공개 범위 산정 |
| 범위 결정 — "sns-admin 프론트와 기능 일치", "커머스 기능 제외" | 요구사항 정의서·로드맵 작성 |
| 제약 — "실무 코드 수정 금지", "민감 정보 비공개" | 참조만 하고 새로 작성, 비밀값은 환경변수·예시 파일로 분리 |
| 진행 방식 — "Phase 단위로 커밋해 고도화 과정을 보여줄 것" | Phase별 구현 → 빌드 검증 → 로드맵 갱신 → 커밋·푸시 |
| 승인과 반려 — 범위 축소, 이력 재작성, 방향 수정 | 수정 반영, 재검증 |

### 활용한 도구

| 도구 | 용도 |
|---|---|
| **Claude Code** | 코드·문서·커밋 메시지 작성, 빌드 실행 및 검증 |
| **커스텀 스킬 `portfolio-setup`** | 진행 중인 실무 프로젝트를 분석해 빈 뼈대 + 요구사항 정의서 + 로드맵을 갖춘 포트폴리오 프로젝트로 구성하는 작업을 재사용 가능한 절차로 정리 |
| **서브 에이전트** | 참조 프로젝트 탐색·분석 |
| **Gradle 빌드** | Phase마다 `./gradlew build`로 컴파일 검증 후 커밋 |

### 진행 방식

```
1. 실무 프로젝트(비공개) 분석        → 모듈·엔티티·API 인벤토리
2. 범위 정의                         → 연계 프론트(sns-admin) 기준으로 SNS 기능만 선별
3. 문서 우선                         → 요구사항 정의서 · 로드맵 작성
4. 빈 뼈대에서 시작                  → 소스 복사 없이 Phase별로 새로 작성
5. Phase 단위 커밋                   → 빌드 검증 → 로드맵·README 갱신 → 커밋·푸시
```

### 솔직한 한계

- **사람의 판단을 대체하지는 않았습니다.** 범위·방향 결정은 전부 프롬프트에서 나왔습니다.
- **잘 아는 도메인이라 가능했습니다.** 같은 서비스를 실무에서 먼저 개발해 두었기에 결과물의 옳고 그름을 검수할 수 있었습니다.

---

## 모듈 구조

```
showme-backend/
├── core/        공유 모듈 — Entity, Repository, 공통 응답 형식, QueryDSL
├── api-admin/   관리자 API (포트 8081)
└── api-user/    사용자 API (포트 8080)
```

---

## 기술 스택

| 분류 | 기술 |
|------|------|
| Language | Java 21 |
| Framework | Spring Boot 3.4.1 |
| Build | Gradle 8 (Multi-module) |
| ORM | Spring Data JPA + QueryDSL 5.1 |
| DB | MySQL 8.4 |
| Auth | JWT (jjwt 0.12.6) · Spring Security 6 |
| Docs | SpringDoc OpenAPI (Swagger UI) |
| Push | Firebase Admin SDK (FCM) |
| Storage | AWS S3 (Spring Cloud AWS) |
| Realtime | Spring WebSocket + STOMP |

---

## 개발 진행 현황

| Phase | 내용 | 상태 |
|-------|------|------|
| 1 | 프로젝트 셋업 & core 모듈 | ✅ 완료 |
| 2 | api-admin 인증 & Spring Security | ⬜ 예정 |
| 3 | api-admin 회원 관리 | ⬜ 예정 |
| 4 | api-admin 피드·신고·공지·FAQ | ⬜ 예정 |
| 5 | api-admin 대시보드 통계 | ⬜ 예정 |
| 6 | api-user 인증 & 회원 & 팔로우 | ⬜ 예정 |
| 7 | api-user 게시물 & 댓글 & 검색 (S3 업로드) | ⬜ 예정 |
| 8 | api-user 알림(FCM) & 채팅(WebSocket) & 고객센터 | ⬜ 예정 |
| 9 | 테스트 & CI/CD & 마무리 | ⬜ 예정 |

상세 항목은 [개발 로드맵](docs/roadmap.md) 참고.

---

## 개발 문서

- [개발 요구사항 정의서](docs/requirements.md)
- [개발 로드맵](docs/roadmap.md)

---

## 로컬 실행

### 사전 요구사항

- JDK 21
- Docker & Docker Compose

### DB 컨테이너 기동

```bash
docker-compose -f docker-compose.dev.yml up -d
```

### 환경 설정

```bash
# 예시 파일 복사 후 값 입력
cp api-admin/src/main/resources/application-local.yml.example \
   api-admin/src/main/resources/application-local.yml
```

### 실행

```bash
# api-admin (포트 8081)
./gradlew :api-admin:bootRun --args='--spring.profiles.active=local'

# api-user (포트 8080)
./gradlew :api-user:bootRun --args='--spring.profiles.active=local'
```

Swagger UI:
- 관리자: http://localhost:8081/swagger-ui/index.html
- 사용자: http://localhost:8080/swagger-ui/index.html

---

## 브랜치 전략

```
main        기준 브랜치 — PR 병합만
feature/*   기능 개발
fix/*       버그 수정
docs/*      문서 작업
```
