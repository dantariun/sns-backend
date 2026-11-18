package com.showme.admin.report.dto;

import com.showme.core.entity.report.*;

import java.time.LocalDateTime;

public record ReportResponse(
        Long id,
        ReportTargetType targetType,
        Long targetId,
        ReporterResponse reporter,
        String reason,
        ReportStatus status,
        ReportAction processedAction,
        String processedMemo,
        Long processedBy,
        LocalDateTime processedAt,
        LocalDateTime createdAt
) {
    public static ReportResponse of(Report r, ReporterResponse reporter) {
        return new ReportResponse(r.getId(), r.getTargetType(), r.getTargetId(), reporter, r.getReason(),
                r.getStatus(), r.getProcessedAction(), r.getProcessedMemo(), r.getProcessedBy(),
                r.getProcessedAt(), r.getCreatedAt());
    }
}
