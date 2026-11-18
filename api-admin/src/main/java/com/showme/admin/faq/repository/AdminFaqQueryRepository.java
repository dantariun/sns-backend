package com.showme.admin.faq.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.showme.admin.faq.dto.FaqSearchCondition;
import com.showme.core.entity.faq.Faq;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static com.showme.core.entity.faq.QFaq.faq;

@Repository
@RequiredArgsConstructor
public class AdminFaqQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Page<Faq> search(FaqSearchCondition cond, Pageable pageable) {
        BooleanExpression[] where = {
                faq.deletedAt.isNull(),
                StringUtils.hasText(cond.keyword())
                        ? faq.question.contains(cond.keyword()).or(faq.answer.contains(cond.keyword())) : null,
                cond.category() != null ? faq.category.eq(cond.category()) : null
        };

        List<Faq> content = queryFactory.selectFrom(faq)
                .where(where)
                .orderBy(faq.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory.select(faq.count()).from(faq).where(where).fetchOne();
        return new PageImpl<>(content, pageable, Optional.ofNullable(total).orElse(0L));
    }
}
