package com.showme.admin.report.dto;

import com.showme.core.entity.report.ReportAction;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/** suspendDays 는 SUSPEND 일 때만 사용, 생략하면 무기한 */
public record ProcessReportRequest(
        @NotNull(message = "처리 방식을 선택해주세요.") ReportAction action,
        @Size(max = 500) String memo,
        @Positive(message = "정지 기간은 1일 이상이어야 합니다.") Integer suspendDays
) {
}
