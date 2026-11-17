package com.showme.admin.member.dto;

import com.showme.core.entity.member.MemberStatus;

import java.time.LocalDateTime;

public record MemberRelationResponse(
        Long id,
        String nickname,
        String profileImageUrl,
        MemberStatus status,
        LocalDateTime followedAt
) {
}
