package com.showme.admin.member.dto;

import com.showme.core.entity.member.MemberStatus;

import java.time.LocalDateTime;

public record MemberSummaryResponse(
        Long id,
        String nickname,
        String email,
        String profileImageUrl,
        String bio,
        MemberStatus status,
        long followersCount,
        long followingsCount,
        long feedCount,
        LocalDateTime createdAt,
        LocalDateTime lastLoginAt
) {
}
