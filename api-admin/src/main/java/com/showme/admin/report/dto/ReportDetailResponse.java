package com.showme.admin.report.dto;

public record ReportDetailResponse(
        ReportResponse report,
        ReportTargetResponse target,
        long sameTargetReportCount
) {
}
