package com.showme.admin.report.service;

import com.querydsl.core.Tuple;
import com.showme.admin.common.dto.AuthorResponse;
import com.showme.admin.post.dto.FeedStatus;
import com.showme.admin.report.dto.*;
import com.showme.admin.report.repository.AdminReportQueryRepository;
import com.showme.core.common.PageResponse;
import com.showme.core.entity.member.Member;
import com.showme.core.entity.member.MemberSanction;
import com.showme.core.entity.member.SanctionType;
import com.showme.core.entity.post.Post;
import com.showme.core.entity.post.PostComment;
import com.showme.core.entity.report.Report;
import com.showme.core.entity.report.ReportAction;
import com.showme.core.exception.BusinessException;
import com.showme.core.exception.ErrorCode;
import com.showme.core.repository.member.MemberRepository;
import com.showme.core.repository.member.MemberSanctionRepository;
import com.showme.core.repository.post.PostCommentRepository;
import com.showme.core.repository.post.PostRepository;
import com.showme.core.repository.report.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.showme.core.entity.member.QMember.member;
import static com.showme.core.entity.report.QReport.report;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminReportService {

    private final ReportRepository reportRepository;
    private final PostRepository postRepository;
    private final PostCommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final MemberSanctionRepository sanctionRepository;
    private final AdminReportQueryRepository queryRepository;

    public PageResponse<ReportResponse> search(ReportSearchCondition cond, Pageable pageable) {
        return PageResponse.of(queryRepository.search(cond, pageable).map(this::toResponse));
    }

    public ReportDetailResponse getDetail(Long reportId) {
        Report r = findReport(reportId);
        return new ReportDetailResponse(
                toResponse(r),
                resolveTarget(r),
                reportRepository.countByTargetTypeAndTargetId(r.getTargetType(), r.getTargetId()));
    }

    /** 같은 대상에 접수된 신고 전체 (최신순) — 누적 신고와 이전 처리 결과 확인용 */
    public List<ReportResponse> getHistory(Long reportId) {
        Report r = findReport(reportId);
        List<Report> reports = reportRepository.findByTargetTypeAndTargetIdOrderByCreatedAtDesc(
                r.getTargetType(), r.getTargetId());
        Map<Long, Member> reporters = memberRepository.findAllById(
                        reports.stream().map(Report::getReporterId).distinct().toList())
                .stream().collect(Collectors.toMap(Member::getId, Function.identity()));

        return reports.stream()
                .map(x -> ReportResponse.of(x, toReporter(reporters.get(x.getReporterId()), x.getReporterId())))
                .toList();
    }

    @Transactional
    public void process(Long reportId, ProcessReportRequest request, Long adminId) {
        Report r = findReport(reportId);
        if (!r.isPending()) throw new BusinessException(ErrorCode.REPORT_ALREADY_PROCESSED);

        switch (request.action()) {
            case BLIND -> blindTarget(r);
            case SUSPEND -> suspendTargetMember(r, request, adminId);
            case WARNING, DISMISS -> { }
        }
        r.process(request.action(), request.memo(), adminId);
    }

    private void blindTarget(Report r) {
        switch (r.getTargetType()) {
            case FEED -> findTargetPost(r.getTargetId()).blind();
            case COMMENT -> findTargetComment(r.getTargetId()).blind();
            case MEMBER -> throw new BusinessException(ErrorCode.INVALID_REPORT_ACTION);
        }
    }

    private void suspendTargetMember(Report r, ProcessReportRequest request, Long adminId) {
        Long memberId = switch (r.getTargetType()) {
            case MEMBER -> r.getTargetId();
            case FEED -> findTargetPost(r.getTargetId()).getMemberId();
            case COMMENT -> findTargetComment(r.getTargetId()).getMemberId();
        };
        Member target = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.REPORT_TARGET_NOT_FOUND));

        // 이미 정지·탈퇴 상태면 제재를 중복으로 걸지 않고 신고만 처리 완료한다
        if (target.isSuspended() || target.isWithdrawn()) return;

        LocalDateTime until = request.suspendDays() != null
                ? LocalDateTime.now().plusDays(request.suspendDays()) : null;
        target.suspend(until);
        sanctionRepository.save(MemberSanction.builder()
                .memberId(memberId)
                .type(SanctionType.SUSPEND)
                .reason("신고 #" + r.getId() + " 처리" + (request.memo() != null ? ": " + request.memo() : ""))
                .suspendedUntil(until)
                .adminId(adminId)
                .build());
    }

    private ReportTargetResponse resolveTarget(Report r) {
        return switch (r.getTargetType()) {
            case FEED -> postRepository.findById(r.getTargetId())
                    .map(p -> new ReportTargetResponse(true, authorOf(p.getMemberId()), p.getContent(),
                            p.isDeleted() ? "DELETED" : FeedStatus.of(p).name()))
                    .orElseGet(ReportTargetResponse::missing);
            case COMMENT -> commentRepository.findById(r.getTargetId())
                    .map(c -> new ReportTargetResponse(true, authorOf(c.getMemberId()), c.getContent(),
                            c.isDeleted() ? "DELETED" : c.isBlinded() ? "BLINDED" : "VISIBLE"))
                    .orElseGet(ReportTargetResponse::missing);
            case MEMBER -> memberRepository.findById(r.getTargetId())
                    .map(m -> new ReportTargetResponse(true, AuthorResponse.from(m), m.getBio(), m.getStatus().name()))
                    .orElseGet(ReportTargetResponse::missing);
        };
    }

    private AuthorResponse authorOf(Long memberId) {
        return memberRepository.findById(memberId)
                .map(AuthorResponse::from)
                .orElseGet(() -> AuthorResponse.unknown(memberId));
    }

    private Post findTargetPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.REPORT_TARGET_NOT_FOUND));
    }

    private PostComment findTargetComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.REPORT_TARGET_NOT_FOUND));
    }

    private Report findReport(Long reportId) {
        return reportRepository.findById(reportId)
                .orElseThrow(() -> new BusinessException(ErrorCode.REPORT_NOT_FOUND));
    }

    private ReportResponse toResponse(Tuple t) {
        Report r = t.get(report);
        return ReportResponse.of(r, toReporter(t.get(member), r.getReporterId()));
    }

    private ReportResponse toResponse(Report r) {
        return ReportResponse.of(r, toReporter(memberRepository.findById(r.getReporterId()).orElse(null), r.getReporterId()));
    }

    private static ReporterResponse toReporter(Member m, Long id) {
        return new ReporterResponse(id, m != null ? m.getNickname() : null);
    }
}
