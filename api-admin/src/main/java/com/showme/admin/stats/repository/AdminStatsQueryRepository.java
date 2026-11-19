package com.showme.admin.stats.repository;

import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.DateTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.showme.core.entity.member.MemberStatus;
import com.showme.core.entity.report.ReportStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

import static com.showme.core.entity.member.QMember.member;
import static com.showme.core.entity.post.QPost.post;
import static com.showme.core.entity.report.QReport.report;

@Repository
@RequiredArgsConstructor
public class AdminStatsQueryRepository {

    private final JPAQueryFactory queryFactory;

    public long countActiveMembers() {
        return value(queryFactory.select(member.count()).from(member)
                .where(member.status.ne(MemberStatus.WITHDRAWN)).fetchOne());
    }

    public long countSignupsBetween(LocalDateTime from, LocalDateTime to) {
        return value(queryFactory.select(member.count()).from(member)
                .where(member.createdAt.goe(from), member.createdAt.lt(to)).fetchOne());
    }

    public long countVisiblePosts() {
        return value(queryFactory.select(post.count()).from(post)
                .where(post.deletedAt.isNull()).fetchOne());
    }

    public long countPendingReports() {
        return value(queryFactory.select(report.count()).from(report)
                .where(report.status.eq(ReportStatus.PENDING)).fetchOne());
    }

    /** 날짜 → 가입자 수. 가입자가 없는 날은 결과에 없다 */
    public Map<LocalDate, Long> countSignupsByDay(LocalDate start, LocalDate end) {
        DateTemplate<LocalDate> day = toDate(member.createdAt);
        return queryFactory.select(day, member.count())
                .from(member)
                .where(member.createdAt.goe(start.atStartOfDay()), member.createdAt.lt(end.plusDays(1).atStartOfDay()))
                .groupBy(day)
                .fetch().stream()
                .collect(Collectors.toMap(t -> t.get(day), t -> t.get(member.count())));
    }

    /** 날짜 → 작성된 게시물 수 (이후 삭제된 게시물 포함 — 그날 작성된 양을 본다) */
    public Map<LocalDate, Long> countPostsByDay(LocalDate start, LocalDate end) {
        DateTemplate<LocalDate> day = toDate(post.createdAt);
        return queryFactory.select(day, post.count())
                .from(post)
                .where(post.createdAt.goe(start.atStartOfDay()), post.createdAt.lt(end.plusDays(1).atStartOfDay()))
                .groupBy(day)
                .fetch().stream()
                .collect(Collectors.toMap(t -> t.get(day), t -> t.get(post.count())));
    }

    private static DateTemplate<LocalDate> toDate(DateTimePath<LocalDateTime> path) {
        return Expressions.dateTemplate(LocalDate.class, "cast({0} as LocalDate)", path);
    }

    private static long value(Long v) {
        return v != null ? v : 0L;
    }
}
