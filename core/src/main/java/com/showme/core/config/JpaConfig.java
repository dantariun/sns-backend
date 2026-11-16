package com.showme.core.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

// 앱 메인 클래스가 com.showme.admin / com.showme.user 에 있어 core 패키지는 자동 스캔 대상이 아니다
@Configuration
@EnableJpaAuditing
@EntityScan(basePackages = "com.showme.core.entity")
@EnableJpaRepositories(basePackages = "com.showme.core.repository")
public class JpaConfig {
}
