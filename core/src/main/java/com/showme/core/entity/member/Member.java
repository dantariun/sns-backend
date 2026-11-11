package com.showme.core.entity.member;

import com.showme.core.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String loginId;

    @Column(nullable = false, length = 20)
    private String nickname;

    @Column(length = 20)
    private String realName;

    @Column(nullable = false, length = 100)
    private String passwordHash;

    @Column(length = 20)
    private String phoneNo;

    @Column(length = 100)
    private String bio;

    @Column(length = 200)
    private String profileImageUrl;

    @Column(length = 255)
    private String pushToken;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private MemberStatus status = MemberStatus.ACTIVE;

    private LocalDateTime lastLoginAt;

    private LocalDateTime withdrawalRequestedAt;

    private LocalDateTime deletedAt;

    @Builder
    public Member(String loginId, String nickname, String realName,
                  String passwordHash, String phoneNo) {
        this.loginId = loginId;
        this.nickname = nickname;
        this.realName = realName;
        this.passwordHash = passwordHash;
        this.phoneNo = phoneNo;
        this.status = MemberStatus.ACTIVE;
    }

    public boolean isDeleted() { return this.deletedAt != null; }
    public boolean isSuspended() { return this.status == MemberStatus.SUSPENDED; }
    public boolean isWithdrawalPending() {
        return this.withdrawalRequestedAt != null && this.deletedAt == null;
    }

    public void updateLastLoginAt() { this.lastLoginAt = LocalDateTime.now(); }

    public void suspend() { this.status = MemberStatus.SUSPENDED; }
    public void activate() { this.status = MemberStatus.ACTIVE; }

    public void requestWithdrawal() { this.withdrawalRequestedAt = LocalDateTime.now(); }
    public void cancelWithdrawal() { this.withdrawalRequestedAt = null; }

    public void withdraw() {
        this.status = MemberStatus.WITHDRAWN;
        this.deletedAt = LocalDateTime.now();
    }

    public void restore() {
        this.status = MemberStatus.ACTIVE;
        this.deletedAt = null;
        this.withdrawalRequestedAt = null;
    }

    public void updateProfile(String nickname, String bio, String profileImageUrl) {
        if (nickname != null) this.nickname = nickname;
        if (bio != null) this.bio = bio;
        if (profileImageUrl != null) this.profileImageUrl = profileImageUrl;
    }

    public void updatePasswordHash(String newPasswordHash) {
        this.passwordHash = newPasswordHash;
    }

    public void updatePushToken(String pushToken) {
        this.pushToken = pushToken;
    }
}
