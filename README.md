# ShowMe Backend

SNS 커머스 플랫폼 백엔드 프로젝트.  
Spring Boot 멀티모듈 구조로 관리자 API, 사용자 API, 쇼핑몰 크롤러를 단일 레포지토리에서 운영한다.

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
| AI | Anthropic Claude Java SDK |
| Realtime | Spring WebSocket + STOMP |

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
