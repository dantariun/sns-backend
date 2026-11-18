package com.showme.admin.post.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/** keyword 는 본문·작성자 닉네임 부분 일치, 날짜는 작성일 기준 양 끝 포함 */
public record FeedSearchCondition(
        String keyword,
        FeedStatus status,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
) {
}
