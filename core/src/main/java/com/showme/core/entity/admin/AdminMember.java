package com.showme.core.entity.admin;

import com.showme.core.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "admin_member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminMember extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String loginId;

    @Column(nullable = false, length = 100)
    private String passwordHash;

    @Column(nullable = false, length = 30)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private AdminRole role;

    @Column(nullable = false)
    private boolean active = true;

    private LocalDateTime lastLoginAt;

    @Builder
    public AdminMember(String loginId, String passwordHash, String name, AdminRole role) {
        this.loginId = loginId;
        this.passwordHash = passwordHash;
        this.name = name;
        this.role = role != null ? role : AdminRole.ADMIN;
        this.active = true;
    }

    public void updateLastLoginAt() { this.lastLoginAt = LocalDateTime.now(); }
}
