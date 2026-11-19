package com.showme.admin.stats.service;

import com.showme.admin.member.dto.MemberSearchCondition;
import com.showme.admin.member.dto.MemberSummaryResponse;
import com.showme.admin.member.repository.AdminMemberQueryRepository;
import com.showme.admin.report.dto.ReportResponse;
import com.showme.admin.report.dto.ReportSearchCondition;
import com.showme.admin.report.service.AdminReportService;
import com.showme.admin.stats.dto.DailyCount;
import com.showme.admin.stats.dto.StatsSummaryResponse;
import com.showme.admin.stats.dto.TrendRequest;
import com.showme.admin.stats.repository.AdminStatsQueryRepository;
import com.showme.core.entity.report.ReportStatus;
import com.showme.core.exception.BusinessException;
import com.showme.core.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminStatsService {

    private static final int DEFAULT_TREND_DAYS = 14;
    private static final int MAX_TREND_DAYS = 90;

    private final AdminStatsQueryRepository statsRepository;
    private final AdminMemberQueryRepository memberQueryRepository;
    private final AdminReportService reportService;

    public StatsSummaryResponse getSummary() {
        LocalDate today = LocalDate.now();
        return new StatsSummaryResponse(
                statsRepository.countActiveMembers(),
                statsRepository.countSignupsBetween(today.atStartOfDay(), today.plusDays(1).atStartOfDay()),
                statsRepository.countVisiblePosts(),
                statsRepository.countPendingReports());
    }

    public List<DailyCount> getSignupTrend(TrendRequest request) {
        return trend(request, statsRepository::countSignupsByDay);
    }

    public List<DailyCount> getPostTrend(TrendRequest request) {
        return trend(request, statsRepository::countPostsByDay);
    }

    public List<ReportResponse> getRecentPendingReports(int size) {
        return reportService.search(new ReportSearchCondition(ReportStatus.PENDING, null), PageRequest.of(0, size))
                .getContent();
    }

    public List<MemberSummaryResponse> getRecentMembers(int size) {
        return memberQueryRepository.search(new MemberSearchCondition(null, null, null, null), PageRequest.of(0, size))
                .getContent();
    }

    /** 집계가 없는 날도 0 으로 채워 차트가 끊기지 않게 한다 */
    private List<DailyCount> trend(TrendRequest request, BiFunction<LocalDate, LocalDate, Map<LocalDate, Long>> counter) {
        LocalDate end = request.endDate() != null ? request.endDate() : LocalDate.now();
        LocalDate start = request.startDate() != null ? request.startDate() : end.minusDays(DEFAULT_TREND_DAYS - 1);
        if (start.isAfter(end) || ChronoUnit.DAYS.between(start, end) >= MAX_TREND_DAYS) {
            throw new BusinessException(ErrorCode.INVALID_DATE_RANGE);
        }

        Map<LocalDate, Long> counts = counter.apply(start, end);
        return start.datesUntil(end.plusDays(1))
                .map(d -> new DailyCount(d, counts.getOrDefault(d, 0L)))
                .toList();
    }
}
