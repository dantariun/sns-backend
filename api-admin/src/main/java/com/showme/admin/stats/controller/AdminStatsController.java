package com.showme.admin.stats.controller;

import com.showme.admin.member.dto.MemberSummaryResponse;
import com.showme.admin.report.dto.ReportResponse;
import com.showme.admin.stats.dto.DailyCount;
import com.showme.admin.stats.dto.StatsSummaryResponse;
import com.showme.admin.stats.dto.TrendRequest;
import com.showme.admin.stats.service.AdminStatsService;
import com.showme.core.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "대시보드 통계")
@RestController
@RequestMapping("/admin/stats")
@RequiredArgsConstructor
public class AdminStatsController {

    private final AdminStatsService statsService;

    @Operation(summary = "통계 요약 카드", description = "총 회원(탈퇴 제외), 오늘 가입자, 총 게시물(삭제 제외), 미처리 신고")
    @GetMapping("/summary")
    public ApiResponse<StatsSummaryResponse> summary() {
        return ApiResponse.ok(statsService.getSummary());
    }

    @Operation(summary = "가입자 추이", description = "일별 가입자 수. 기본 최근 14일, 최대 90일. 빈 날은 0")
    @GetMapping("/members/trend")
    public ApiResponse<List<DailyCount>> signupTrend(@ParameterObject TrendRequest request) {
        return ApiResponse.ok(statsService.getSignupTrend(request));
    }

    @Operation(summary = "게시물 추이", description = "일별 작성 게시물 수. 기본 최근 14일, 최대 90일. 빈 날은 0")
    @GetMapping("/posts/trend")
    public ApiResponse<List<DailyCount>> postTrend(@ParameterObject TrendRequest request) {
        return ApiResponse.ok(statsService.getPostTrend(request));
    }

    @Operation(summary = "미처리 신고 현황", description = "최근 접수된 미처리 신고")
    @GetMapping("/reports/pending")
    public ApiResponse<List<ReportResponse>> pendingReports(
            @RequestParam(defaultValue = "5") @Min(1) @Max(50) int size) {
        return ApiResponse.ok(statsService.getRecentPendingReports(size));
    }

    @Operation(summary = "최근 가입 회원")
    @GetMapping("/members/recent")
    public ApiResponse<List<MemberSummaryResponse>> recentMembers(
            @RequestParam(defaultValue = "5") @Min(1) @Max(50) int size) {
        return ApiResponse.ok(statsService.getRecentMembers(size));
    }
}
