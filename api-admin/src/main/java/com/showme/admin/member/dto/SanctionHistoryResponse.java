package com.showme.admin.member.dto;

import com.showme.core.entity.member.MemberSanction;
import com.showme.core.entity.member.SanctionType;

import java.time.LocalDateTime;

public record SanctionHistoryResponse(
        Long id,
        SanctionType type,
        String reason,
        LocalDateTime suspendedUntil,
        Long adminId,
        LocalDateTime createdAt
) {
    public static SanctionHistoryResponse from(MemberSanction s) {
        return new SanctionHistoryResponse(s.getId(), s.getType(), s.getReason(),
                s.getSuspendedUntil(), s.getAdminId(), s.getCreatedAt());
    }
}
