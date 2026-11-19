package com.showme.admin.stats.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/** 둘 다 생략하면 오늘을 포함한 최근 14일, 양 끝 포함 */
public record TrendRequest(
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
) {
}
