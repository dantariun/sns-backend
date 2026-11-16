package com.showme.core.entity.admin;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "admin_refresh_token")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminRefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long adminId;

    @Column(nullable = false, unique = true, length = 500)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Builder
    public AdminRefreshToken(Long adminId, String token, LocalDateTime expiresAt) {
        this.adminId = adminId;
        this.token = token;
        this.expiresAt = expiresAt;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }
}
