package com.showme.admin.faq.dto;

import com.showme.core.entity.faq.FaqCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record FaqRequest(
        @NotNull(message = "카테고리를 선택해주세요.") FaqCategory category,
        @NotBlank(message = "질문을 입력해주세요.") @Size(max = 300, message = "질문은 300자 이하여야 합니다.") String question,
        @NotBlank(message = "답변을 입력해주세요.") String answer
) {
}
