package com.showme.admin.notice.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.showme.admin.notice.dto.NoticeSearchCondition;
import com.showme.core.entity.notice.Notice;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static com.showme.core.entity.notice.QNotice.notice;

@Repository
@RequiredArgsConstructor
public class AdminNoticeQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Page<Notice> search(NoticeSearchCondition cond, Pageable pageable) {
        BooleanExpression[] where = {
                notice.deletedAt.isNull(),
                StringUtils.hasText(cond.keyword()) ? notice.title.contains(cond.keyword()) : null,
                cond.published() != null ? notice.published.eq(cond.published()) : null
        };

        List<Notice> content = queryFactory.selectFrom(notice)
                .where(where)
                .orderBy(notice.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory.select(notice.count()).from(notice).where(where).fetchOne();
        return new PageImpl<>(content, pageable, Optional.ofNullable(total).orElse(0L));
    }
}
