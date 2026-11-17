package com.showme.admin.member.dto;

import com.showme.core.entity.member.MemberStatus;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/** keyword 는 닉네임·이메일(loginId) 부분 일치, 날짜는 가입일 기준 양 끝 포함 */
public record MemberSearchCondition(
        String keyword,
        MemberStatus status,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
) {
}
