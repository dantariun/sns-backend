package com.showme.admin.member.dto;

import com.showme.core.entity.member.Member;
import com.showme.core.entity.member.MemberStatus;

import java.time.LocalDateTime;

public record MemberDetailResponse(
        Long id,
        String nickname,
        String email,
        String realName,
        String phoneNo,
        String profileImageUrl,
        String bio,
        MemberStatus status,
        LocalDateTime suspendedUntil,
        long followersCount,
        long followingsCount,
        long feedCount,
        LocalDateTime createdAt,
        LocalDateTime lastLoginAt,
        LocalDateTime withdrawalRequestedAt,
        LocalDateTime deletedAt
) {
    public static MemberDetailResponse of(Member m, long followers, long followings, long feeds) {
        return new MemberDetailResponse(
                m.getId(), m.getNickname(), m.getLoginId(), m.getRealName(), m.getPhoneNo(),
                m.getProfileImageUrl(), m.getBio(), m.getStatus(), m.getSuspendedUntil(),
                followers, followings, feeds,
                m.getCreatedAt(), m.getLastLoginAt(), m.getWithdrawalRequestedAt(), m.getDeletedAt());
    }
}
