package com.showme.admin.faq.controller;

import com.showme.admin.faq.dto.FaqRequest;
import com.showme.admin.faq.dto.FaqResponse;
import com.showme.admin.faq.dto.FaqSearchCondition;
import com.showme.admin.faq.service.AdminFaqService;
import com.showme.core.common.ApiResponse;
import com.showme.core.common.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "FAQ")
@RestController
@RequestMapping("/admin/faqs")
@RequiredArgsConstructor
public class AdminFaqController {

    private final AdminFaqService faqService;

    @Operation(summary = "FAQ 목록", description = "질문·답변 검색, 카테고리 필터")
    @GetMapping
    public ApiResponse<PageResponse<FaqResponse>> search(
            @ParameterObject FaqSearchCondition condition,
            @ParameterObject @PageableDefault(size = 20) Pageable pageable) {
        return ApiResponse.ok(faqService.search(condition, pageable));
    }

    @Operation(summary = "FAQ 상세")
    @GetMapping("/{id}")
    public ApiResponse<FaqResponse> detail(@PathVariable Long id) {
        return ApiResponse.ok(faqService.getDetail(id));
    }

    @Operation(summary = "FAQ 작성", description = "생성된 FAQ id 반환")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Long> create(@Valid @RequestBody FaqRequest request) {
        return ApiResponse.ok(faqService.create(request));
    }

    @Operation(summary = "FAQ 수정")
    @PutMapping("/{id}")
    public ApiResponse<Void> update(@PathVariable Long id, @Valid @RequestBody FaqRequest request) {
        faqService.update(id, request);
        return ApiResponse.ok();
    }

    @Operation(summary = "FAQ 삭제", description = "소프트 삭제")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        faqService.delete(id);
        return ApiResponse.ok();
    }
}
