package com.showme.admin.notice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.showme.core.entity.notice.Notice;

import java.time.LocalDateTime;

public record NoticeResponse(
        Long id,
        String title,
        String content,
        @JsonProperty("isPublished") boolean isPublished,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static NoticeResponse from(Notice n) {
        return new NoticeResponse(n.getId(), n.getTitle(), n.getContent(), n.isPublished(),
                n.getCreatedAt(), n.getUpdatedAt());
    }
}
