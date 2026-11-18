package com.showme.admin.post.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.showme.admin.post.dto.FeedSearchCondition;
import com.showme.admin.post.dto.FeedStatus;
import com.showme.admin.post.dto.LikerResponse;
import com.showme.core.entity.post.PostVisibility;
import com.showme.core.entity.report.ReportTargetType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.showme.core.entity.member.QMember.member;
import static com.showme.core.entity.post.QPost.post;
import static com.showme.core.entity.post.QPostLike.postLike;
import static com.showme.core.entity.report.QReport.report;

@Repository
@RequiredArgsConstructor
public class AdminPostQueryRepository {

    private final JPAQueryFactory queryFactory;

    /** 삭제되지 않은 게시물을 작성자와 함께 조회한다. 작성자가 없으면 member 는 null */
    public Page<Tuple> search(FeedSearchCondition cond, Pageable pageable) {
        BooleanExpression[] where = {
                post.deletedAt.isNull(),
                keywordContains(cond.keyword()),
                statusEq(cond.status()),
                createdFrom(cond.startDate()),
                createdTo(cond.endDate())
        };

        List<Tuple> content = queryFactory
                .select(post, member)
                .from(post)
                .leftJoin(member).on(member.id.eq(post.memberId))
                .where(where)
                .orderBy(post.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory.select(post.count())
                .from(post)
                .leftJoin(member).on(member.id.eq(post.memberId))
                .where(where)
                .fetchOne();
        return new PageImpl<>(content, pageable, Optional.ofNullable(total).orElse(0L));
    }

    /** 게시물 id → 신고 수 (신고가 없는 id 는 결과에 없음) */
    public Map<Long, Long> countReports(Collection<Long> postIds) {
        if (postIds.isEmpty()) return Map.of();
        return queryFactory
                .select(report.targetId, report.count())
                .from(report)
                .where(report.targetType.eq(ReportTargetType.FEED), report.targetId.in(postIds))
                .groupBy(report.targetId)
                .fetch().stream()
                .collect(Collectors.toMap(t -> t.get(report.targetId), t -> t.get(report.count())));
    }

    public Page<LikerResponse> findLikers(Long postId, Pageable pageable) {
        List<LikerResponse> content = queryFactory
                .select(Projections.constructor(LikerResponse.class,
                        postLike.memberId, member.nickname, member.profileImageUrl, postLike.createdAt))
                .from(postLike)
                .leftJoin(member).on(member.id.eq(postLike.memberId))
                .where(postLike.postId.eq(postId))
                .orderBy(postLike.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory.select(postLike.count()).from(postLike)
                .where(postLike.postId.eq(postId)).fetchOne();
        return new PageImpl<>(content, pageable, Optional.ofNullable(total).orElse(0L));
    }

    private BooleanExpression keywordContains(String keyword) {
        if (!StringUtils.hasText(keyword)) return null;
        return post.content.contains(keyword).or(member.nickname.contains(keyword));
    }

    private BooleanExpression statusEq(FeedStatus status) {
        if (status == null) return null;
        return switch (status) {
            case BLINDED -> post.blinded.isTrue();
            case PUBLIC -> post.blinded.isFalse().and(post.visibility.eq(PostVisibility.PUBLIC));
            case PRIVATE -> post.blinded.isFalse().and(post.visibility.eq(PostVisibility.PRIVATE));
        };
    }

    private BooleanExpression createdFrom(LocalDate start) {
        return start != null ? post.createdAt.goe(start.atStartOfDay()) : null;
    }

    private BooleanExpression createdTo(LocalDate end) {
        return end != null ? post.createdAt.lt(end.plusDays(1).atStartOfDay()) : null;
    }
}
