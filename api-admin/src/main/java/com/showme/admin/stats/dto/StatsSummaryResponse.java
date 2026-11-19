package com.showme.admin.stats.dto;

/** totalMembers 는 탈퇴 회원 제외, totalFeeds 는 삭제 게시물 제외 */
public record StatsSummaryResponse(
        long totalMembers,
        long todaySignups,
        long totalFeeds,
        long pendingReports
) {
}
