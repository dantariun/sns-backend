package com.showme.admin.faq.dto;

import com.showme.core.entity.faq.FaqCategory;

/** keyword 는 질문·답변 부분 일치 */
public record FaqSearchCondition(String keyword, FaqCategory category) {
}
