package com.showme.core.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // ── 공통 ──────────────────────────────────────────────────────
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "리소스를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다."),

    // ── 인증 ──────────────────────────────────────────────────────
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 리프레시 토큰입니다."),

    // ── 회원 ──────────────────────────────────────────────────────
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."),
    DUPLICATE_LOGIN_ID(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "이미 사용 중인 닉네임입니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 올바르지 않습니다."),
    WITHDRAWN_MEMBER(HttpStatus.FORBIDDEN, "탈퇴한 회원입니다."),
    WITHDRAWAL_PENDING(HttpStatus.FORBIDDEN, "탈퇴 처리 중인 계정입니다."),
    SUSPENDED_MEMBER(HttpStatus.FORBIDDEN, "정지된 회원입니다."),
    CANNOT_FOLLOW_SELF(HttpStatus.BAD_REQUEST, "자기 자신을 팔로우할 수 없습니다."),
    ALREADY_FOLLOWING(HttpStatus.CONFLICT, "이미 팔로우한 회원입니다."),
    NOT_FOLLOWING(HttpStatus.BAD_REQUEST, "팔로우하지 않은 회원입니다."),
    CANNOT_BLOCK_SELF(HttpStatus.BAD_REQUEST, "자기 자신을 차단할 수 없습니다."),
    ALREADY_BLOCKED(HttpStatus.CONFLICT, "이미 차단한 회원입니다."),

    // ── SMS OTP ───────────────────────────────────────────────────
    UNREGISTERED_PHONE(HttpStatus.NOT_FOUND, "가입된 계정이 없습니다."),
    SMS_OTP_NOT_REQUESTED(HttpStatus.BAD_REQUEST, "인증번호 발송 내역이 없습니다."),
    SMS_OTP_EXPIRED(HttpStatus.BAD_REQUEST, "인증번호가 만료되었습니다."),
    SMS_OTP_CODE_MISMATCH(HttpStatus.BAD_REQUEST, "인증번호가 올바르지 않습니다."),

    // ── 게시물 ────────────────────────────────────────────────────
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "게시물을 찾을 수 없습니다."),
    POST_ACCESS_DENIED(HttpStatus.FORBIDDEN, "게시물에 접근 권한이 없습니다."),
    ALREADY_LIKED(HttpStatus.CONFLICT, "이미 좋아요한 게시물입니다."),
    NOT_LIKED(HttpStatus.BAD_REQUEST, "좋아요하지 않은 게시물입니다."),
    ALREADY_BOOKMARKED(HttpStatus.CONFLICT, "이미 북마크한 게시물입니다."),
    BOOKMARK_NOT_FOUND(HttpStatus.BAD_REQUEST, "북마크하지 않은 게시물입니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."),
    COMMENT_ACCESS_DENIED(HttpStatus.FORBIDDEN, "댓글에 접근 권한이 없습니다."),
    COMMENT_REPLY_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "대댓글에는 답글을 달 수 없습니다."),
    COMMENT_ALREADY_LIKED(HttpStatus.CONFLICT, "이미 좋아요한 댓글입니다."),
    COMMENT_NOT_LIKED(HttpStatus.BAD_REQUEST, "좋아요하지 않은 댓글입니다."),

    // ── 채팅 ──────────────────────────────────────────────────────
    CHAT_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "채팅방을 찾을 수 없습니다."),
    CHAT_ROOM_ACCESS_DENIED(HttpStatus.FORBIDDEN, "채팅방에 접근 권한이 없습니다."),
    CHAT_CANNOT_SELF(HttpStatus.BAD_REQUEST, "자기 자신과 채팅할 수 없습니다."),
    CHAT_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "채팅 인증에 실패했습니다."),

    // ── 검색 ──────────────────────────────────────────────────────
    SEARCH_LOG_NOT_FOUND(HttpStatus.NOT_FOUND, "검색 기록을 찾을 수 없습니다."),

    // ── 알림 ──────────────────────────────────────────────────────
    NEWS_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "알림을 찾을 수 없습니다."),

    // ── 파일 ──────────────────────────────────────────────────────
    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다."),
    INVALID_FILE_TYPE(HttpStatus.BAD_REQUEST, "지원하지 않는 파일 형식입니다."),
    FILE_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "파일 크기를 초과했습니다."),

    // ── 관리자 ────────────────────────────────────────────────────
    ADMIN_NOT_FOUND(HttpStatus.NOT_FOUND, "관리자를 찾을 수 없습니다."),
    NOT_ADMIN_ROLE(HttpStatus.FORBIDDEN, "관리자 계정이 아닙니다."),
    SANCTION_NOT_FOUND(HttpStatus.NOT_FOUND, "제재 내역을 찾을 수 없습니다."),
    MEMBER_ALREADY_WITHDRAWN(HttpStatus.BAD_REQUEST, "이미 탈퇴한 회원입니다."),
    MEMBER_NOT_WITHDRAWN(HttpStatus.BAD_REQUEST, "탈퇴 상태가 아닌 회원입니다."),
    REPORT_NOT_FOUND(HttpStatus.NOT_FOUND, "신고 내역을 찾을 수 없습니다."),
    REPORT_ALREADY_PROCESSED(HttpStatus.BAD_REQUEST, "이미 처리된 신고입니다."),
    NOTICE_NOT_FOUND(HttpStatus.NOT_FOUND, "공지사항을 찾을 수 없습니다."),
    FAQ_NOT_FOUND(HttpStatus.NOT_FOUND, "FAQ를 찾을 수 없습니다."),
    INQUIRY_NOT_FOUND(HttpStatus.NOT_FOUND, "문의를 찾을 수 없습니다."),
    INQUIRY_ACCESS_DENIED(HttpStatus.FORBIDDEN, "문의에 접근 권한이 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
