package com.showme.admin.report.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.showme.admin.report.dto.ReportSearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.showme.core.entity.member.QMember.member;
import static com.showme.core.entity.report.QReport.report;

@Repository
@RequiredArgsConstructor
public class AdminReportQueryRepository {

    private final JPAQueryFactory queryFactory;

    /** (report, 신고자 member) 튜플. 신고자가 없으면 member 는 null */
    public Page<Tuple> search(ReportSearchCondition cond, Pageable pageable) {
        BooleanExpression[] where = {
                cond.status() != null ? report.status.eq(cond.status()) : null,
                cond.targetType() != null ? report.targetType.eq(cond.targetType()) : null
        };

        List<Tuple> content = queryFactory
                .select(report, member)
                .from(report)
                .leftJoin(member).on(member.id.eq(report.reporterId))
                .where(where)
                .orderBy(report.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory.select(report.count()).from(report).where(where).fetchOne();
        return new PageImpl<>(content, pageable, Optional.ofNullable(total).orElse(0L));
    }
}
