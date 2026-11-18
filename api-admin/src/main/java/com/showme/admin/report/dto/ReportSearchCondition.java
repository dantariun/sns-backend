package com.showme.admin.report.dto;

import com.showme.core.entity.report.ReportStatus;
import com.showme.core.entity.report.ReportTargetType;

public record ReportSearchCondition(ReportStatus status, ReportTargetType targetType) {
}
