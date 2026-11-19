package com.showme.admin.stats.dto;

import java.time.LocalDate;

public record DailyCount(LocalDate date, long count) {
}
