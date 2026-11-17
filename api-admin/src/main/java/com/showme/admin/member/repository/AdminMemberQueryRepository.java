package com.showme.admin.member.repository;

import com.showme.admin.member.dto.MemberRelationResponse;
import com.showme.admin.member.dto.MemberSearchCondition;
import com.showme.admin.member.dto.MemberSummaryResponse;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.showme.core.entity.member.MemberStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.showme.core.entity.follow.QFollow.follow;
import static com.showme.core.entity.member.QMember.member;
import static com.showme.core.entity.post.QPost.post;

@Repository
@RequiredArgsConstructor
public class AdminMemberQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Page<MemberSummaryResponse> search(MemberSearchCondition cond, Pageable pageable) {
        BooleanExpression[] where = {
                keywordContains(cond.keyword()),
                statusEq(cond.status()),
                joinedFrom(cond.startDate()),
                joinedTo(cond.endDate())
        };

        List<MemberSummaryResponse> content = queryFactory
                .select(Projections.constructor(MemberSummaryResponse.class,
                        member.id, member.nickname, member.loginId, member.profileImageUrl, member.bio,
                        member.status,
                        followersCount(), followingsCount(), feedCount(),
                        member.createdAt, member.lastLoginAt))
                .from(member)
                .where(where)
                .orderBy(member.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory.select(member.count()).from(member).where(where).fetchOne();
        return new PageImpl<>(content, pageable, Optional.ofNullable(total).orElse(0L));
    }

    public long countFollowers(Long memberId) {
        return count(queryFactory.select(follow.count()).from(follow)
                .where(follow.followingId.eq(memberId)).fetchOne());
    }

    public long countFollowings(Long memberId) {
        return count(queryFactory.select(follow.count()).from(follow)
                .where(follow.followerId.eq(memberId)).fetchOne());
    }

    public long countFeeds(Long memberId) {
        return count(queryFactory.select(post.count()).from(post)
                .where(post.memberId.eq(memberId), post.deletedAt.isNull()).fetchOne());
    }

    /** 이 회원을 팔로우하는 회원 목록 */
    public Page<MemberRelationResponse> findFollowers(Long memberId, Pageable pageable) {
        List<MemberRelationResponse> content = relationQuery(follow.followerId, follow.followingId.eq(memberId), pageable);
        return new PageImpl<>(content, pageable, countFollowers(memberId));
    }

    /** 이 회원이 팔로우하는 회원 목록 */
    public Page<MemberRelationResponse> findFollowings(Long memberId, Pageable pageable) {
        List<MemberRelationResponse> content = relationQuery(follow.followingId, follow.followerId.eq(memberId), pageable);
        return new PageImpl<>(content, pageable, countFollowings(memberId));
    }

    private List<MemberRelationResponse> relationQuery(NumberPath<Long> joinColumn,
                                                       BooleanExpression filter, Pageable pageable) {
        return queryFactory
                .select(Projections.constructor(MemberRelationResponse.class,
                        member.id, member.nickname, member.profileImageUrl, member.status, follow.createdAt))
                .from(follow)
                .join(member).on(member.id.eq(joinColumn))
                .where(filter)
                .orderBy(follow.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private Expression<Long> followersCount() {
        return Expressions.as(JPAExpressions.select(follow.count()).from(follow)
                .where(follow.followingId.eq(member.id)), "followersCount");
    }

    private Expression<Long> followingsCount() {
        return Expressions.as(JPAExpressions.select(follow.count()).from(follow)
                .where(follow.followerId.eq(member.id)), "followingsCount");
    }

    private Expression<Long> feedCount() {
        return Expressions.as(JPAExpressions.select(post.count()).from(post)
                .where(post.memberId.eq(member.id), post.deletedAt.isNull()), "feedCount");
    }

    private BooleanExpression keywordContains(String keyword) {
        if (!StringUtils.hasText(keyword)) return null;
        return member.nickname.contains(keyword).or(member.loginId.contains(keyword));
    }

    private BooleanExpression statusEq(MemberStatus status) {
        return status != null ? member.status.eq(status) : null;
    }

    private BooleanExpression joinedFrom(LocalDate start) {
        return start != null ? member.createdAt.goe(start.atStartOfDay()) : null;
    }

    private BooleanExpression joinedTo(LocalDate end) {
        return end != null ? member.createdAt.lt(end.plusDays(1).atStartOfDay()) : null;
    }

    private static long count(Long value) {
        return value != null ? value : 0L;
    }
}
