package com.showme.core.entity.report;

public enum ReportAction {
    WARNING,  // 경고만 기록
    BLIND,    // 대상 게시물·댓글 블라인드
    SUSPEND,  // 대상 작성자(또는 회원) 계정 정지
    DISMISS   // 기각
}
