package com.showme.admin.faq.dto;

import com.showme.core.entity.faq.Faq;
import com.showme.core.entity.faq.FaqCategory;

import java.time.LocalDateTime;

public record FaqResponse(
        Long id,
        FaqCategory category,
        String question,
        String answer,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static FaqResponse from(Faq f) {
        return new FaqResponse(f.getId(), f.getCategory(), f.getQuestion(), f.getAnswer(),
                f.getCreatedAt(), f.getUpdatedAt());
    }
}
