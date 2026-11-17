package com.showme.admin.member.service;

import com.showme.admin.member.dto.*;
import com.showme.admin.member.repository.AdminMemberQueryRepository;
import com.showme.core.common.PageResponse;
import com.showme.core.entity.member.Member;
import com.showme.core.entity.member.MemberSanction;
import com.showme.core.entity.member.SanctionType;
import com.showme.core.exception.BusinessException;
import com.showme.core.exception.ErrorCode;
import com.showme.core.repository.member.MemberRepository;
import com.showme.core.repository.member.MemberSanctionRepository;
import com.showme.core.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminMemberService {

    private final MemberRepository memberRepository;
    private final MemberSanctionRepository sanctionRepository;
    private final PostRepository postRepository;
    private final AdminMemberQueryRepository queryRepository;

    public PageResponse<MemberSummaryResponse> search(MemberSearchCondition cond, Pageable pageable) {
        return PageResponse.of(queryRepository.search(cond, pageable));
    }

    public MemberDetailResponse getDetail(Long memberId) {
        Member member = findMember(memberId);
        return MemberDetailResponse.of(member,
                queryRepository.countFollowers(memberId),
                queryRepository.countFollowings(memberId),
                queryRepository.countFeeds(memberId));
    }

    public PageResponse<MemberPostResponse> getPosts(Long memberId, Pageable pageable) {
        findMember(memberId);
        return PageResponse.of(postRepository.findByMemberId(memberId, pageable).map(MemberPostResponse::from));
    }

    public PageResponse<MemberRelationResponse> getFollowers(Long memberId, Pageable pageable) {
        findMember(memberId);
        return PageResponse.of(queryRepository.findFollowers(memberId, pageable));
    }

    public PageResponse<MemberRelationResponse> getFollowings(Long memberId, Pageable pageable) {
        findMember(memberId);
        return PageResponse.of(queryRepository.findFollowings(memberId, pageable));
    }

    public List<SanctionHistoryResponse> getSanctionHistory(Long memberId) {
        findMember(memberId);
        return sanctionRepository.findByMemberIdOrderByCreatedAtDesc(memberId).stream()
                .map(SanctionHistoryResponse::from)
                .toList();
    }

    @Transactional
    public void suspend(Long memberId, SuspendRequest request, Long adminId) {
        Member member = findMember(memberId);
        if (member.isWithdrawn()) throw new BusinessException(ErrorCode.MEMBER_ALREADY_WITHDRAWN);
        if (member.isSuspended()) throw new BusinessException(ErrorCode.MEMBER_ALREADY_SUSPENDED);

        LocalDateTime until = request.days() != null ? LocalDateTime.now().plusDays(request.days()) : null;
        member.suspend(until);
        record(memberId, SanctionType.SUSPEND, request.reason(), until, adminId);
    }

    @Transactional
    public void releaseSuspension(Long memberId, String reason, Long adminId) {
        Member member = findMember(memberId);
        if (!member.isSuspended()) throw new BusinessException(ErrorCode.MEMBER_NOT_SUSPENDED);

        member.releaseSuspension();
        record(memberId, SanctionType.RELEASE, reason, null, adminId);
    }

    @Transactional
    public void forceWithdraw(Long memberId, String reason, Long adminId) {
        Member member = findMember(memberId);
        if (member.isWithdrawn()) throw new BusinessException(ErrorCode.MEMBER_ALREADY_WITHDRAWN);

        member.withdraw();
        record(memberId, SanctionType.FORCED_WITHDRAW, reason, null, adminId);
    }

    @Transactional
    public void restore(Long memberId, String reason, Long adminId) {
        Member member = findMember(memberId);
        if (!member.isWithdrawn()) throw new BusinessException(ErrorCode.MEMBER_NOT_WITHDRAWN);

        member.restore();
        record(memberId, SanctionType.RESTORE, reason, null, adminId);
    }

    private Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
    }

    private void record(Long memberId, SanctionType type, String reason, LocalDateTime until, Long adminId) {
        sanctionRepository.save(MemberSanction.builder()
                .memberId(memberId)
                .type(type)
                .reason(reason)
                .suspendedUntil(until)
                .adminId(adminId)
                .build());
    }
}
