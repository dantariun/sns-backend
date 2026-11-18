package com.showme.admin.report.controller;

import com.showme.admin.report.dto.*;
import com.showme.admin.report.service.AdminReportService;
import com.showme.core.common.ApiResponse;
import com.showme.core.common.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "신고 관리")
@RestController
@RequestMapping("/admin/reports")
@RequiredArgsConstructor
public class AdminReportController {

    private final AdminReportService reportService;

    @Operation(summary = "신고 목록", description = "처리 상태·대상 유형 필터")
    @GetMapping
    public ApiResponse<PageResponse<ReportResponse>> search(
            @ParameterObject ReportSearchCondition condition,
            @ParameterObject @PageableDefault(size = 20) Pageable pageable) {
        return ApiResponse.ok(reportService.search(condition, pageable));
    }

    @Operation(summary = "신고 상세", description = "신고 내용 + 대상 요약 + 같은 대상의 누적 신고 수")
    @GetMapping("/{id}")
    public ApiResponse<ReportDetailResponse> detail(@PathVariable Long id) {
        return ApiResponse.ok(reportService.getDetail(id));
    }

    @Operation(summary = "처리 이력", description = "같은 대상에 접수된 신고와 각 처리 결과")
    @GetMapping("/{id}/history")
    public ApiResponse<List<ReportResponse>> history(@PathVariable Long id) {
        return ApiResponse.ok(reportService.getHistory(id));
    }

    @Operation(summary = "신고 처리",
            description = "WARNING: 기록만 / BLIND: 게시물·댓글 블라인드 / SUSPEND: 대상 회원(작성자) 정지 / DISMISS: 기각")
    @PatchMapping("/{id}")
    public ApiResponse<Void> process(@PathVariable Long id,
                                     @Valid @RequestBody ProcessReportRequest request,
                                     @AuthenticationPrincipal Long adminId) {
        reportService.process(id, request, adminId);
        return ApiResponse.ok();
    }
}
