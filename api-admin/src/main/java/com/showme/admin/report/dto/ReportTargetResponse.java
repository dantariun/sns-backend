package com.showme.admin.report.dto;

import com.showme.admin.common.dto.AuthorResponse;

/**
 * 신고 대상 요약.
 * author 는 게시물·댓글이면 작성자, 회원이면 그 회원 자신.
 * state 는 FEED: PUBLIC·PRIVATE·BLINDED·DELETED / COMMENT: VISIBLE·BLINDED·DELETED / MEMBER: 회원 상태
 */
public record ReportTargetResponse(
        boolean exists,
        AuthorResponse author,
        String content,
        String state
) {
    public static ReportTargetResponse missing() {
        return new ReportTargetResponse(false, null, null, null);
    }
}
